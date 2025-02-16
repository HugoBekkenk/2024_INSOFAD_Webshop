
import com.example.gamewebshop.controller.PromoCodeController;
import com.example.gamewebshop.models.PromoCode;
import com.example.gamewebshop.services.PromoCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PromoCodeControllerTest {

    @Mock
    private PromoCodeService promoCodeService;

    @InjectMocks
    private PromoCodeController promoCodeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchAllPromoCodes() {
        PromoCode promoCode1 = new PromoCode("PROMO10", 10.0, LocalDateTime.now().plusDays(10), 5, PromoCode.PromoCodeType.PERCENTAGE);
        PromoCode promoCode2 = new PromoCode("PROMO20", 20.0, LocalDateTime.now().plusDays(10), 3, PromoCode.PromoCodeType.FIXED_AMOUNT);

        List<PromoCode> promoCodes = Arrays.asList(promoCode1, promoCode2);

        when(promoCodeService.retrieveAllPromoCodes()).thenReturn(promoCodes);

        ResponseEntity<List<PromoCode>> response = promoCodeController.fetchAllPromoCodes();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("PROMO10", response.getBody().get(0).getCode());
        assertEquals("PROMO20", response.getBody().get(1).getCode());
    }
}
