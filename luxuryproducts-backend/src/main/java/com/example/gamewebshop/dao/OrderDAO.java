package com.example.gamewebshop.dao;

import com.example.gamewebshop.dto.*;
import com.example.gamewebshop.models.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class OrderDAO {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;
    private final OptionsRepository optionsRepository;
    private final ProductOrderRepository productOrderRepository;
    private final VariantOrderRepository variantOrderRepository;
    private final UserGiftCardDAO userGiftCardDAO;
    private final PromoCodeRepository promoCodeRepository;
    private final GiftCardRepository giftCardRepository;

    public OrderDAO(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, VariantRepository variantRepository, OptionsRepository optionsRepository, ProductOrderRepository productOrderRepository, VariantOrderRepository variantOrderRepository, UserGiftCardDAO userGiftCardDAO, PromoCodeRepository promoCodeRepository, GiftCardRepository giftCardRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.optionsRepository = optionsRepository;
        this.productOrderRepository = productOrderRepository;
        this.variantOrderRepository = variantOrderRepository;
        this.userGiftCardDAO = userGiftCardDAO;
        this.promoCodeRepository = promoCodeRepository;
        this.giftCardRepository = giftCardRepository;
    }

    public List<PlacedOrder> getAllOrders() {
        return this.orderRepository.findAll();
    }

    @Transactional
    public void createOrder(PlacedOrder placedOrder) {
        this.userRepository.save(placedOrder.getUser());
        this.orderRepository.save(placedOrder);
    }

    private Set<ProductOrder> transformOrderDTOIntoProductOrder(OrderDTO orderDTO) {
        Set<ProductOrder> productOrders = new HashSet<>();

        for (ProductOrderDTO productDTO : orderDTO.productDTOS) {
            Optional<Product> optionalProduct = productRepository.findById(productDTO.productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                if (product.getStock().intValue() <= 0){
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Product is out of stock"
                    );
                }
                Set<VariantOrder> variantOrders = new HashSet<>();
                if(Objects.equals(product.getType(), "PRODUCT")){
                    for (VariantOrderDTO variantDTO : productDTO.variantOrders) {
                        Optional<Variant> optionalVariant = variantRepository.findById(variantDTO.variantId);
                        if (optionalVariant.isPresent()) {
                            Variant variant = optionalVariant.get();
                            Optional<Options> optionalOption = optionsRepository.findById(variantDTO.selectedOptionId);
                            if (optionalOption.isPresent()) {
                                Options option = optionalOption.get();
                                VariantOrder variantOrder = new VariantOrder(variant.getName(), option.getName(), option.getPriceAdded());
                                this.variantOrderRepository.save(variantOrder);
                                variantOrders.add(variantOrder);
                            } else {
                                throw new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, "No option found with that option id: " + variantDTO.selectedOptionId
                                );
                            }
                        } else {
                            throw new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "No variant found with that variant id: " + variantDTO.variantId
                            );
                        }
                    }
                }

                ProductOrder productOrder = new ProductOrder(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getImgURL(),
                        product.getSpecifications(),
                        product.getReleaseDate(),
                        product.getPublisher(),
                        product.getCategory(),
                        variantOrders,
                        product.getType()
                );
                Number stock = product.getStock().intValue() - 1;
                product.setStock(stock);
                this.productOrderRepository.save(productOrder);
                productOrders.add(productOrder);
            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No product found with that id, id: " + productDTO.productId
                );
            }
        }

        return productOrders;
    }
    private OrderResponseDTO placeOrder(PlacedOrder order, OrderDTO orderDTO, CustomUser user) {
        order.setGiftCardIds(orderDTO.getGiftCardIds());
        int totalProducts = order.getProducts().size();
        order.setTotalProducts(totalProducts);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        double totalAmount = 0;
        double giftCardAmount = 0;

        for (ProductOrder product : order.getProducts()) {
            if (Objects.equals(product.getType(), "GIFT_CARD")){
                Optional<GiftCard> giftCard = giftCardRepository.findGiftCardByName(product.getName());
                if (giftCard.isPresent()){
                    userGiftCardDAO.saveUserGiftCard(order.getUser().getEmail(), giftCard.get().getId());
                }
            }
            if (Objects.nonNull(product.getName())) {
                totalAmount += product.getPrice().doubleValue();
            }
        }

        if (Objects.nonNull(orderDTO.getGiftCardIds()) && !orderDTO.getGiftCardIds().isEmpty()) {
            List<UserGiftCard> userGiftCards = userGiftCardDAO.getAllUserGiftCardsByGiftCardIds(order.getGiftCardIds());
            if (Objects.isNull(userGiftCards) || userGiftCards.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilized gift cards don't belong this user");
            }
            for (UserGiftCard userGiftCard : userGiftCards) {
                if (Objects.nonNull(userGiftCard.getGiftCard())) {
                    giftCardAmount += userGiftCard.getGiftCard().getAmount();
                }
            }
            long userGiftCardsSize = userGiftCards.size();
            double usedAmountPerGiftCard = totalAmount / userGiftCardsSize;
            for (UserGiftCard userGiftCard : userGiftCards) {
                if (usedAmountPerGiftCard > giftCardAmount) {
                    userGiftCard.getGiftCard().setAmount(0.0);
                } else {
                    userGiftCard.getGiftCard().setAmount(userGiftCard.getGiftCard().getAmount() - usedAmountPerGiftCard);
                }
            }
            for (UserGiftCard userGiftCard : userGiftCards) {
                if (userGiftCard.getGiftCard().getAmount() < 0) {
                    userGiftCard.getGiftCard().setAmount(0.0);
                }
            }
            userGiftCardDAO.removeUsedGiftCards(userGiftCards.stream().map(ug -> ug.getGiftCard().getId()).toList());
        }
        order.setPaidAmountByGiftCard(giftCardAmount);
        order.setName(orderDTO.getName());
        order.setLast_name(orderDTO.getLast_name());
        order.setInfix(orderDTO.getInfix());
        order.setZipcode(orderDTO.getZipcode());
        order.setHouseNumber(orderDTO.getHouseNumber());
        order.setNotes(orderDTO.getNotes());
        order.setTotalAmount(totalAmount);
        OrderResponseDTO response = new OrderResponseDTO();
        response.setTotalAmount(totalAmount);
        response.setUsedAmount(giftCardAmount);
        double remainingAmount = totalAmount - giftCardAmount;
        if (remainingAmount < 0) {
            response.setRemainingAmount(0.0);
        } else {
            response.setRemainingAmount(remainingAmount);
        }
        response.setMessage("Order created successfully");
        orderRepository.save(order);
        return response;
    }

    @Transactional
    public OrderResponseDTO saveOrderWithProducts(OrderDTO orderDTO, String userEmail) {
        Set<ProductOrder> productOrders = transformOrderDTOIntoProductOrder(orderDTO);

        Optional<CustomUser> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        CustomUser user = userOptional.get();
        PlacedOrder order = new PlacedOrder();
        order.setProducts(productOrders);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        double totalPrice= calculateTotalPrice(orderDTO);
        double giftCardAmount = 0;

        if (Objects.nonNull(orderDTO.getGiftCardIds()) && !orderDTO.getGiftCardIds().isEmpty()) {
            giftCardAmount = handleGiftCardPayments(order, orderDTO);
        }
        order.setPaidAmountByGiftCard(giftCardAmount);

        Optional<PromoCode> promoCodeOptional = promoCodeRepository.findByCode(orderDTO.promoCode);
        if (promoCodeOptional.isPresent()) {
            PromoCode promoCode = promoCodeOptional.get();
            double giftcardPrice = 0;
            if (promoCode.getType().toString().equals("PERCENTAGE")){
                for (ProductOrder product: order.getProducts()) {
                    if (Objects.equals(product.getType(), "GIFT_CARD")){
                        giftcardPrice += product.getPrice().doubleValue();
                        totalPrice -= product.getPrice().doubleValue();
                    }
                }
            }
            double discount = applyPromoCode(totalPrice, promoCode);
            order.setDiscountedPrice(totalPrice - discount);
            order.setDiscountedPrice(order.getDiscountedPrice() + giftcardPrice);
            totalPrice += giftcardPrice;

        } else {
            order.setDiscountedPrice(order.getTotalPrice());
        }

        order.setTotalPrice(totalPrice);
        order.setPromoCode(orderDTO.promoCode);
        return placeOrder(order, orderDTO, user);
    }

    private double calculateTotalPrice(OrderDTO orderDTO) {
        double totalPrice = 0;
        for (ProductOrderDTO productDTO : orderDTO.getProductDTOS()) {
            Optional<Product> optionalProduct = productRepository.findById(productDTO.productId);
            if (optionalProduct.isEmpty()){
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No product found with that id"
                );
            }
            Product product = optionalProduct.get();
            totalPrice += product.getPrice().doubleValue();
            for (VariantOrderDTO variantOrderDTO : productDTO.variantOrders) {
                Optional<Options> selectedOptionOptional = optionsRepository.findById(variantOrderDTO.selectedOptionId);
                if (selectedOptionOptional.isPresent()) {
                    Options selectedOption = selectedOptionOptional.get();
                    totalPrice += selectedOption.getPriceAdded().doubleValue();
                }
            }
        }
//        }else{
//            for (GiftCard giftCard : orderDTO.getGiftCards()) {
//                totalPrice+= giftCard.getAmount();
//            }
//        }
        return totalPrice;
    }


    private double handleGiftCardPayments(PlacedOrder order, OrderDTO orderDTO) {
        double giftCardAmount = 0;
        List<UserGiftCard> userGiftCards = userGiftCardDAO.getAllUserGiftCardsByGiftCardIds(order.getGiftCardIds());

        for (UserGiftCard userGiftCard : userGiftCards) {
            if (Objects.nonNull(userGiftCard.getGiftCard())) {
                giftCardAmount += userGiftCard.getGiftCard().getAmount();
            }
        }

        double usedAmountPerGiftCard = order.getTotalPrice() / userGiftCards.size();
        for (UserGiftCard userGiftCard : userGiftCards) {
            if (usedAmountPerGiftCard > giftCardAmount) {
                userGiftCard.getGiftCard().setAmount(0.0);
            } else {
                userGiftCard.getGiftCard().setAmount(userGiftCard.getGiftCard().getAmount() - usedAmountPerGiftCard);
            }
        }

        userGiftCardDAO.removeUsedGiftCards(userGiftCards.stream().map(ug -> ug.getGiftCard().getId()).toList());
        return giftCardAmount;
    }

    private double applyPromoCode(double totalPrice, PromoCode promoCode) {
        double discount = 0;
        if (promoCode.getType().toString().equals("FIXED_AMOUNT")) {
            discount = promoCode.getDiscount();
        } else if (promoCode.getType().toString().equals("PERCENTAGE")) {
            discount = (totalPrice / 100) * promoCode.getDiscount();
        }
        return discount;
    }

    public List<PlacedOrder> getOrdersByUserId(long userId){
        Optional<List<PlacedOrder>> orderList = this.orderRepository.findByUserId(userId);
        if (orderList.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No user found with that id"
            );
        }
        return orderList.get();
    }




}
