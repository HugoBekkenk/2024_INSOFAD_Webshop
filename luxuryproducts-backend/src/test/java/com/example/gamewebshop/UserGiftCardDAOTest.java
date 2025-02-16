package com.example.gamewebshop;

import com.example.gamewebshop.dao.GiftCardDAO;
import com.example.gamewebshop.dao.UserDAO;
import com.example.gamewebshop.dao.UserGiftCardDAO;
import com.example.gamewebshop.dao.UserGiftCardRepository;
import com.example.gamewebshop.dto.MiniGiftCardDTO;
import com.example.gamewebshop.dto.UserGiftCardDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.GiftCard;
import com.example.gamewebshop.models.UserGiftCard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserGiftCardDAOTest {

    @Mock
    private UserGiftCardRepository userGiftCardRepository;

    @Mock
    private UserDAO userDAO;

    @Mock
    private GiftCardDAO giftCardDAO;

    @InjectMocks
    private UserGiftCardDAO userGiftCardDAO;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Test
    void testSaveUserGiftCard() {
        GiftCard giftCard = new GiftCard();
        CustomUser receivedBy = new CustomUser();
        CustomUser sendBy = new CustomUser();
        sendBy.setEmail("test@test.com");

        when(giftCardDAO.getGiftCardById(1L)).thenReturn(giftCard);
        when(userDAO.getUserByEmail("received@test.com")).thenReturn(receivedBy);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("send@test.com");
        when(userDAO.getUserByEmail("send@test.com")).thenReturn(sendBy);
        SecurityContextHolder.setContext(securityContext);

        userGiftCardDAO.saveUserGiftCard("received@test.com", 1L);

        verify(userGiftCardRepository).save(any(UserGiftCard.class));
        verify(giftCardDAO).deleteById(1L);
    }

    @Test
    void testGetAllGiftCardsSentByUser() {
        UserGiftCard userGiftCard1 = new UserGiftCard();
        UserGiftCard userGiftCard2 = new UserGiftCard();
        when(userGiftCardRepository.findAllBySendById(1L)).thenReturn(Arrays.asList(userGiftCard1, userGiftCard2));

        List<UserGiftCard> result = userGiftCardDAO.getAllGiftCardsSentByUser(1L);

        assertThat(result).hasSize(2);
        assertThat(result).contains(userGiftCard1, userGiftCard2);
    }

    @Test
    void testGetAllGiftCardsReceivedByUser() {
        UserGiftCard userGiftCard1 = new UserGiftCard();
        UserGiftCard userGiftCard2 = new UserGiftCard();
        when(userGiftCardRepository.findAllByReceivedById(1L)).thenReturn(Arrays.asList(userGiftCard1, userGiftCard2));

        List<UserGiftCard> result = userGiftCardDAO.getAllGiftCardsReceivedByUser(1L);

        assertThat(result).hasSize(2);
        assertThat(result).contains(userGiftCard1, userGiftCard2);
    }

    @Test
    void testGetAllUserGiftCardsByGiftCardIds() {
        UserGiftCard userGiftCard1 = new UserGiftCard();
        UserGiftCard userGiftCard2 = new UserGiftCard();
        when(userGiftCardRepository.findAllByActiveTrueAndGiftCardIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(userGiftCard1, userGiftCard2));

        List<UserGiftCard> result = userGiftCardDAO.getAllUserGiftCardsByGiftCardIds(Arrays.asList(1L, 2L));

        assertThat(result).hasSize(2);
        assertThat(result).contains(userGiftCard1, userGiftCard2);
    }

    @Test
    void testGetAllUserGiftCards() {
        UserGiftCard userGiftCard1 = new UserGiftCard();
        UserGiftCard userGiftCard2 = new UserGiftCard();
        GiftCard giftCard = new GiftCard();
        CustomUser sendBy = new CustomUser();
        CustomUser receivedBy = new CustomUser();
        userGiftCard1.setGiftCard(giftCard);
        userGiftCard1.setSendBy(sendBy);
        userGiftCard1.setReceivedBy(receivedBy);
        userGiftCard1.setActive(true);
        userGiftCard2.setGiftCard(giftCard);
        userGiftCard2.setSendBy(sendBy);
        userGiftCard2.setReceivedBy(receivedBy);
        userGiftCard2.setActive(true);
        when(userGiftCardRepository.findAllByActiveTrue()).thenReturn(Arrays.asList(userGiftCard1, userGiftCard2));

        List<UserGiftCardDTO> result = userGiftCardDAO.getAllUserGiftCars();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getGiftCard()).isEqualTo(giftCard);
        assertThat(result.get(0).getSendBy()).isEqualTo(sendBy);
        assertThat(result.get(0).getReceivedBy()).isEqualTo(receivedBy);
    }

    @Test
    void testGetGiftCardsDropDown() {
        GiftCard giftCard1 = new GiftCard();
        giftCard1.setAmount(100.0);
        giftCard1.setId(1L);
        giftCard1.setCardCode("CODE1");

        UserGiftCard userGiftCard1 = new UserGiftCard();
        userGiftCard1.setGiftCard(giftCard1);

        List<UserGiftCard> userGiftCards = Arrays.asList(userGiftCard1);
        when(userGiftCardRepository.findAllByReceivedById(1L)).thenReturn(userGiftCards);

        List<MiniGiftCardDTO> result = userGiftCardDAO.getGiftCardsDropDown(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getCardCode()).isEqualTo("CODE1");
    }

    @Test
    void testRemoveUsedGiftCards() {
        GiftCard giftCard1 = new GiftCard();
        giftCard1.setAmount(0.0);

        UserGiftCard userGiftCard1 = new UserGiftCard();
        userGiftCard1.setGiftCard(giftCard1);
        userGiftCard1.setActive(true);

        List<UserGiftCard> userGiftCards = Arrays.asList(userGiftCard1);
        when(userGiftCardRepository.findAllByActiveTrueAndGiftCardIdIn(Arrays.asList(1L, 2L))).thenReturn(userGiftCards);

        userGiftCardDAO.removeUsedGiftCards(Arrays.asList(1L, 2L));

        assertThat(userGiftCard1.isActive()).isFalse();
        verify(giftCardDAO).updateUsedGiftCard(giftCard1);
        verify(userGiftCardRepository).saveAll(userGiftCards);
    }
}
