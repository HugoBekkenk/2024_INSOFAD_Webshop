import com.example.gamewebshop.controller.PromoCodeController;
import com.example.gamewebshop.dao.UserRepository;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.PromoCode;
import com.example.gamewebshop.services.PromoCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class PromoCodeSecurityTest {
    @InjectMocks
    private PromoCodeController promoCodeController;

    @Mock
    private PromoCodeService promoCodeService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchAllPromoCodes() {
        List<PromoCode> promoCodes = List.of(new PromoCode());
        when(promoCodeService.retrieveAllPromoCodes()).thenReturn(promoCodes);

        ResponseEntity<List<PromoCode>> response = promoCodeController.fetchAllPromoCodes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(promoCodes, response.getBody());
    }

    @Test
    void createNewPromoCode_AsAdmin() {
        PromoCode promoCode = new PromoCode();
        PromoCode savedPromoCode = new PromoCode();
        savedPromoCode.setId(1L);

        CustomUser adminUser = new CustomUser();
        adminUser.setAuthority("admin");

        when(principal.getName()).thenReturn("admin@example.com");
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(adminUser));
        when(promoCodeService.createPromoCode(promoCode)).thenReturn(savedPromoCode);

        ResponseEntity<PromoCode> response = promoCodeController.createNewPromoCode(promoCode, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedPromoCode, response.getBody());
    }

    @Test
    void createNewPromoCode_AsNonAdmin() {
        PromoCode promoCode = new PromoCode();

        CustomUser user = new CustomUser();
        user.setAuthority("user");

        when(principal.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                promoCodeController.createNewPromoCode(promoCode, principal)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User is unauthorized for this function", exception.getReason());
    }

    @Test
    void removePromoCode_AsAdmin() {
        Long promoCodeId = 1L;

        CustomUser adminUser = new CustomUser();
        adminUser.setAuthority("admin");

        when(principal.getName()).thenReturn("admin@example.com");
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(adminUser));
        when(promoCodeService.checkPromoCodeExistsById(promoCodeId)).thenReturn(true);
        doNothing().when(promoCodeService).removePromoCode(promoCodeId);

        ResponseEntity<?> response = promoCodeController.removePromoCode(promoCodeId, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void removePromoCode_AsNonAdmin() {
        Long promoCodeId = 1L;

        CustomUser user = new CustomUser();
        user.setAuthority("user");

        when(principal.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                promoCodeController.removePromoCode(promoCodeId, principal)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User is unauthorized for this function", exception.getReason());
    }
}
