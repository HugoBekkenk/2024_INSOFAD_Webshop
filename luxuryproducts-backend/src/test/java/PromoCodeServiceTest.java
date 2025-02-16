
import com.example.gamewebshop.dao.PromoCodeRepository;
import com.example.gamewebshop.models.PlacedOrder;
import com.example.gamewebshop.models.PromoCode;
import com.example.gamewebshop.models.PromoCode.PromoCodeType;
import com.example.gamewebshop.services.PromoCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PromoCodeServiceTest {

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @InjectMocks
    private PromoCodeService promoCodeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchPromoCodeByCode() {
        PromoCode promoCode = new PromoCode();
        promoCode.setCode("TESTCODE");
        when(promoCodeRepository.findByCode("TESTCODE")).thenReturn(Optional.of(promoCode));

        Optional<PromoCode> result = promoCodeService.fetchPromoCodeByCode("TESTCODE");

        assertEquals("TESTCODE", result.get().getCode());
        verify(promoCodeRepository, times(1)).findByCode("TESTCODE");
    }

    @Test
    public void testValidatePromoCode_Valid() {
        PromoCode promoCode = new PromoCode();
        promoCode.setCode("VALIDCODE");
        promoCode.setExpiryDate(LocalDateTime.now().plusDays(1));
        promoCode.setMaxUsageCount(5);

        when(promoCodeRepository.findByCode("VALIDCODE")).thenReturn(Optional.of(promoCode));

        boolean isValid = promoCodeService.validatePromoCode("VALIDCODE");

        assertEquals(true, isValid);
        verify(promoCodeRepository, times(1)).findByCode("VALIDCODE");
    }

    @Test
    public void testValidatePromoCode_Invalid() {
        PromoCode promoCode = new PromoCode();
        promoCode.setCode("INVALIDCODE");
        promoCode.setExpiryDate(LocalDateTime.now().minusDays(1));
        promoCode.setMaxUsageCount(5);

        when(promoCodeRepository.findByCode("INVALIDCODE")).thenReturn(Optional.of(promoCode));

        boolean isValid = promoCodeService.validatePromoCode("INVALIDCODE");

        assertEquals(false, isValid);
        verify(promoCodeRepository, times(1)).findByCode("INVALIDCODE");
    }
}
