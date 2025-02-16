package com.example.gamewebshop;

import com.example.gamewebshop.dao.GiftCardDAO;
import com.example.gamewebshop.dao.GiftCardRepository;
import com.example.gamewebshop.dao.ProductRepository;
import com.example.gamewebshop.dto.GiftCardDTO;
import com.example.gamewebshop.models.GiftCard;
import com.example.gamewebshop.models.Product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCardDAOTest {

    @Mock
    private GiftCardRepository giftCardRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GiftCardDAO giftCardDAO;

    @Test
    void testGetAllActiveGiftCards() {
        GiftCard giftCard1 = new GiftCard();
        GiftCard giftCard2 = new GiftCard();
        when(giftCardRepository.findAllByActiveTrue()).thenReturn(Arrays.asList(giftCard1, giftCard2));

        List<GiftCard> result = giftCardDAO.getAllActiveGiftCards();

        assertThat(result).hasSize(2);
        assertThat(result).contains(giftCard1, giftCard2);
    }

    @Test
    void testGetGiftCardById() {
        GiftCard giftCard = new GiftCard();
        when(giftCardRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(giftCard));

        GiftCard result = giftCardDAO.getGiftCardById(1L);

        assertThat(result).isEqualTo(giftCard);
    }

    @Test
    void testGetGiftCardById_NotFound() {
        when(giftCardRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> giftCardDAO.getGiftCardById(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No gift card found with that id");
    }

    @Test
    void testCreateGiftCard() {
        GiftCard giftCard = new GiftCard("Test Card", 100.0, "image.png", true);

        giftCardDAO.createGiftCard(giftCard);

        assertThat(giftCard.getCardCode()).startsWith("GC");
        assertThat(giftCard.isActive()).isTrue();
        verify(giftCardRepository).save(giftCard);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testUpdateUsedGiftCard() {
        GiftCard giftCard = new GiftCard();

        giftCardDAO.updateUsedGiftCard(giftCard);

        verify(giftCardRepository).save(giftCard);
    }

    @Test
    void testUpdateGiftCard() {
        GiftCardDTO giftCardDTO = new GiftCardDTO();
        giftCardDTO.setName("Updated Name");
        giftCardDTO.setAmount(200.0);
        GiftCard giftCard = new GiftCard();
        when(giftCardRepository.findById(1L)).thenReturn(Optional.of(giftCard));

        giftCardDAO.updateGiftCard(giftCardDTO, 1L);

        assertThat(giftCard.getName()).isEqualTo("Updated Name");
        assertThat(giftCard.getAmount()).isEqualTo(200.0);
        assertThat(giftCard.isActive()).isTrue();
        verify(giftCardRepository).save(giftCard);
    }

    @Test
    void testDeleteById() {
        GiftCard giftCard = new GiftCard();
        when(giftCardRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(giftCard));

        giftCardDAO.deleteById(1L);

        assertThat(giftCard.isActive()).isFalse();
        verify(giftCardRepository).save(giftCard);
    }

    @Test
    void testGetAllActiveGiftCardsByIds() {
        GiftCard giftCard1 = new GiftCard();
        GiftCard giftCard2 = new GiftCard();
        when(giftCardRepository.findAllByActiveTrueAndIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(giftCard1, giftCard2));

        List<GiftCard> result = giftCardDAO.getAllActiveGiftCardsByIds(Arrays.asList(1L, 2L));

        assertThat(result).hasSize(2);
        assertThat(result).contains(giftCard1, giftCard2);
    }
}
