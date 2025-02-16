import com.example.gamewebshop.dao.*;
import com.example.gamewebshop.dto.OrderDTO;
import com.example.gamewebshop.dto.OrderResponseDTO;
import com.example.gamewebshop.dto.ProductOrderDTO;
import com.example.gamewebshop.dto.VariantOrderDTO;
import com.example.gamewebshop.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderDAOTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private VariantRepository variantRepository;
    @Mock
    private OptionsRepository optionsRepository;
    @Mock
    private ProductOrderRepository productOrderRepository;
    @Mock
    private VariantOrderRepository variantOrderRepository;
    @Mock
    private UserGiftCardDAO userGiftCardDAO;
    @Mock
    private PromoCodeRepository promoCodeRepository;
    @Mock
    private GiftCardRepository giftCardRepository;
    private OrderDAO SUT;

    private OrderDTO orderDTO;
    private CustomUser user;
    private Product product;
    private Variant variant;
    private Options option;
    private PromoCode promoCode;

    @BeforeEach
    public void setup() {
        this.SUT = new OrderDAO(orderRepository, userRepository, productRepository, variantRepository, optionsRepository, productOrderRepository, variantOrderRepository, userGiftCardDAO, promoCodeRepository, giftCardRepository);
        orderDTO = new OrderDTO();
        user = new CustomUser();
        user.setEmail("test@example.com");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100.0);
        product.setStock(10);
        product.setType("PRODUCT");

        variant = new Variant();
        variant.setId(1L);
        variant.setName("Test Variant");

        option = new Options();
        option.setId(1L);
        option.setName("Test Option");
        option.setPriceAdded(10.0);

        promoCode = new PromoCode();
        promoCode.setCode("PROMO10");
        promoCode.setType(PromoCode.PromoCodeType.PERCENTAGE);
        promoCode.setDiscount(10);

    }

    @Test
    public void testGetAllOrders_Success() {
        List<PlacedOrder> dummyOrders = new ArrayList<>();
        when(this.orderRepository.findAll()).thenReturn(dummyOrders);

        List<PlacedOrder> actualOrders = this.SUT.getAllOrders();

        assertThat(actualOrders, is(dummyOrders));
        Mockito.verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testGetOrdersByUserId_Success() {
        List<PlacedOrder> dummyOrders = new ArrayList<>();
        long dummyId = 1;
        when(this.orderRepository.findByUserId(dummyId)).thenReturn(Optional.of(dummyOrders));

        List<PlacedOrder> actualOrders = this.SUT.getOrdersByUserId(dummyId);

        assertThat(actualOrders, is(dummyOrders));
        Mockito.verify(orderRepository, times(1)).findByUserId(dummyId);
    }

    @Test
    public void testGetOrdersByUserId_NotFoundException() {
        when(this.orderRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
        int dummyId = 1;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> this.SUT.getOrdersByUserId(dummyId));

        assertThat(exception.getMessage(), is("404 NOT_FOUND \"No user found with that id\""));
    }

    @Test
    public void testSaveOrderWithProducts_Success() {
        VariantOrderDTO[]  variantOrderDTOS =  new VariantOrderDTO[1];
        variantOrderDTOS[0] =  new VariantOrderDTO(1L, 1L);
        ProductOrderDTO[] productOrderDTO = new ProductOrderDTO[1];
        productOrderDTO[0] = new ProductOrderDTO(1L, variantOrderDTOS, "PRODUCT");
        orderDTO.setProductDTOS(productOrderDTO);

        lenient().when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        lenient().when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        lenient().when(variantRepository.findById(anyLong())).thenReturn(Optional.of(variant));
        lenient().when(optionsRepository.findById(anyLong())).thenReturn(Optional.of(option));
        lenient().when(promoCodeRepository.findByCode(anyString())).thenReturn(Optional.of(promoCode));

        OrderResponseDTO response = SUT.saveOrderWithProducts(orderDTO, "test@example.com");

        assertNotNull(response);
        assertEquals(100.0, response.getTotalAmount());
        verify(productOrderRepository, times(1)).save(any(ProductOrder.class));
        verify(variantOrderRepository, times(1)).save(any(VariantOrder.class));
        verify(orderRepository, times(1)).save(any(PlacedOrder.class));
    }


    @Test
    public void testSaveOrderWithProducts_ProductNotFound() {
        VariantOrderDTO[]  variantOrderDTOS =  new VariantOrderDTO[1];
        variantOrderDTOS[0] =  new VariantOrderDTO(1L, 1L);
        ProductOrderDTO[] productOrderDTO = new ProductOrderDTO[1];
        productOrderDTO[0] = new ProductOrderDTO(1L, variantOrderDTOS, "PRODUCT");
        orderDTO.setProductDTOS(productOrderDTO);

        lenient().when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        lenient().when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            SUT.saveOrderWithProducts(orderDTO, "test@example.com");
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No product found with that id, id: 1", exception.getReason());
    }

    @Test
    public void testSaveOrderWithProducts_VariantNotFound() {
        VariantOrderDTO[]  variantOrderDTOS =  new VariantOrderDTO[1];
        variantOrderDTOS[0] =  new VariantOrderDTO(1L, 1L);
        ProductOrderDTO[] productOrderDTO = new ProductOrderDTO[1];
        productOrderDTO[0] = new ProductOrderDTO(1L, variantOrderDTOS, "PRODUCT");
        orderDTO.setProductDTOS(productOrderDTO);

        lenient().when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        lenient().when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        lenient().when(variantRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            SUT.saveOrderWithProducts(orderDTO, "test@mail.com");
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No variant found with that variant id: 1", exception.getReason());
    }

    @Test
    public void testSaveOrderWithProducts_OptionNotFound() {
        VariantOrderDTO[]  variantOrderDTOS =  new VariantOrderDTO[1];
        variantOrderDTOS[0] =  new VariantOrderDTO(1L, 1L);
        ProductOrderDTO[] productOrderDTO = new ProductOrderDTO[1];
        productOrderDTO[0] = new ProductOrderDTO(1L, variantOrderDTOS, "PRODUCT");
        orderDTO.setProductDTOS(productOrderDTO);

        lenient().when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        lenient().when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        lenient().when(variantRepository.findById(anyLong())).thenReturn(Optional.of(variant));
        lenient().when(optionsRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            SUT.saveOrderWithProducts(orderDTO, "test@example.com");
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("No option found with that option id: 1", exception.getReason());
    }

    @Test
    public void testSaveOrderWithProducts_ProductOutOfStock() {
        product.setStock(0);
        VariantOrderDTO[]  variantOrderDTOS =  new VariantOrderDTO[1];
        variantOrderDTOS[0] =  new VariantOrderDTO(1L, 1L);
        ProductOrderDTO[] productOrderDTO = new ProductOrderDTO[1];
        productOrderDTO[0] = new ProductOrderDTO(1L, variantOrderDTOS, "PRODUCT");
        orderDTO.setProductDTOS(productOrderDTO);

        lenient().when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        lenient().when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            SUT.saveOrderWithProducts(orderDTO, "test@example.com");
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Product is out of stock", exception.getReason());
    }
}
