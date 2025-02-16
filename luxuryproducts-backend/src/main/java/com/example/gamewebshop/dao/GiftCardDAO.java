package com.example.gamewebshop.dao;

import com.example.gamewebshop.dto.GiftCardDTO;
import com.example.gamewebshop.models.GiftCard;
import com.example.gamewebshop.models.Product;
import com.example.gamewebshop.utils.UniqueCombinationGenerator;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

@Component
public class GiftCardDAO {

    private final GiftCardRepository giftCardRepository;
    private final ProductRepository productRepository;

    public GiftCardDAO(GiftCardRepository giftCardRepository, ProductRepository productRepository){
        this.giftCardRepository=giftCardRepository;
        this.productRepository = productRepository;
    }
    public List<GiftCard> getAllActiveGiftCards(){
        return this.giftCardRepository.findAllByActiveTrue();
    }

    public GiftCard getGiftCardById(long id){
        Optional<GiftCard> giftCard = this.giftCardRepository.findByIdAndActiveTrue(id);
        return giftCard.orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No gift card found with that id"
        ));
    }
    public void createGiftCard(GiftCard giftCard) {
        String cardCode = "GC" + UniqueCombinationGenerator.generateUniqueCombination(10).toUpperCase();
        giftCard.setCardCode(cardCode);
        giftCard.setActive(true);
        this.giftCardRepository.save(giftCard);

        // Save the gift card as a Product
        Product product = new Product(
                giftCard.getName(),
                "Gift Card - ", // Include the card code in the description
                giftCard.getAmount(),
                giftCard.getImage(),
                "Gift Card",
                "", // Release date can be empty or set to null
                "Gift Card Publisher",
                50, // Assuming stock is not relevant for gift cards
                null, // Assuming no category for gift cards
                "GIFT_CARD" // Setting the type to "giftcard"
        );

        this.productRepository.save(product);
    }
    public void updateUsedGiftCard(GiftCard giftCard){
        this.giftCardRepository.save(giftCard);
    }

    public void updateGiftCard(GiftCardDTO giftCardDTO, Long id){
        Optional<GiftCard> giftCard = this.giftCardRepository.findById(id);

        if (giftCard.isPresent()){
            giftCard.get().setName(giftCardDTO.getName());
            giftCard.get().setAmount(giftCardDTO.getAmount());
            giftCard.get().setActive(true);

            this.giftCardRepository.save(giftCard.get());
        }
    }

    @Transactional
    public void deleteById(Long id) {
        GiftCard giftCard = this.getGiftCardById(id);
        giftCard.setActive(false);
        giftCardRepository.save(giftCard);
    }

    public List<GiftCard> getAllActiveGiftCardsByIds(List<Long> giftCardIds){
        return this.giftCardRepository.findAllByActiveTrueAndIdIn(giftCardIds);
    }

}
