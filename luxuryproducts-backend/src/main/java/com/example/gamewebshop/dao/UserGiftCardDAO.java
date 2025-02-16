package com.example.gamewebshop.dao;


import com.example.gamewebshop.dto.MiniGiftCardDTO;
import com.example.gamewebshop.dto.UserGiftCardDTO;
import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.GiftCard;
import com.example.gamewebshop.models.UserGiftCard;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserGiftCardDAO {

    private final UserGiftCardRepository userGiftCardRepository;
    private final UserDAO userDAO;
    private final GiftCardDAO giftCardDAO;

    public UserGiftCardDAO(UserGiftCardRepository userGiftCardRepository, UserDAO userDAO,GiftCardDAO giftCardDAO) {
        this.userGiftCardRepository = userGiftCardRepository;
        this.userDAO = userDAO;
        this.giftCardDAO =  giftCardDAO;
    }

    @Transactional
    public void saveUserGiftCard(String receivedByEmail, Long giftCardId) {
        GiftCard giftCard = giftCardDAO.getGiftCardById(giftCardId);
        CustomUser receivedBy = userDAO.getUserByEmail(receivedByEmail);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser sendBy = userDAO.getUserByEmail(authentication.getName());
        UserGiftCard userGiftCard = new UserGiftCard();
        userGiftCard.setGiftCard(giftCard);
        userGiftCard.setSendBy(sendBy);
        userGiftCard.setReceivedBy(receivedBy);
        userGiftCard.setActive(true);
        userGiftCardRepository.save(userGiftCard);
        giftCardDAO.deleteById(giftCardId);
    }
    public List<UserGiftCard> getAllGiftCardsSentByUser(Long sendById){
        return userGiftCardRepository.findAllBySendById(sendById);
    }
    public List<UserGiftCard> getAllGiftCardsReceivedByUser(Long receivedById){
        return userGiftCardRepository.findAllByReceivedById(receivedById);
    }

    public List<UserGiftCard> getAllUserGiftCardsByGiftCardIds(List<Long> giftCardIds){
        return userGiftCardRepository.findAllByActiveTrueAndGiftCardIdIn(giftCardIds);
    }

    public List<UserGiftCardDTO> getAllUserGiftCars(){
        List<UserGiftCardDTO> userGiftCardDTOS = new ArrayList<>();
        for(UserGiftCard userGiftCard:userGiftCardRepository.findAllByActiveTrue()){
            UserGiftCardDTO userGiftCardDTO = new UserGiftCardDTO();
            userGiftCardDTO.setGiftCard(userGiftCard.getGiftCard());
            userGiftCardDTO.setSendBy(userGiftCard.getSendBy());
            userGiftCardDTO.setReceivedBy(userGiftCard.getReceivedBy());
            userGiftCardDTO.setId(userGiftCard.getId());
            userGiftCardDTOS.add(userGiftCardDTO);
        }
        return userGiftCardDTOS;
    }
    public List<MiniGiftCardDTO> getGiftCardsDropDown(Long receivedById){
        List<UserGiftCard> userGiftCards = userGiftCardRepository.findAllByReceivedById(receivedById);
        List<MiniGiftCardDTO> miniGiftCardDTOS = new ArrayList<>();
        for(UserGiftCard userGiftCard:userGiftCards){
            if(userGiftCard.getGiftCard().getAmount()>0){
                MiniGiftCardDTO miniGiftCardDTO = new MiniGiftCardDTO();
                miniGiftCardDTO.setId(userGiftCard.getGiftCard().getId());
                miniGiftCardDTO.setCardCode(userGiftCard.getGiftCard().getCardCode());
                miniGiftCardDTOS.add(miniGiftCardDTO);
            }
        }
        return miniGiftCardDTOS;
    }
    @Transactional
    public void removeUsedGiftCards(List<Long> giftCardIds){
        List<UserGiftCard> userGiftCards = userGiftCardRepository.findAllByActiveTrueAndGiftCardIdIn(giftCardIds);
        for (UserGiftCard userGiftCard : userGiftCards) {
            giftCardDAO.updateUsedGiftCard(userGiftCard.getGiftCard());
            if(userGiftCard.getGiftCard().getAmount()<=0){
                userGiftCard.setActive(false);
            }
        }
        userGiftCardRepository.saveAll(userGiftCards);
    }

}
