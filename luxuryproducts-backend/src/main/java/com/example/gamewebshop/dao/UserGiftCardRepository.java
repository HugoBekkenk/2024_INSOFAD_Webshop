package com.example.gamewebshop.dao;

import com.example.gamewebshop.models.UserGiftCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGiftCardRepository extends JpaRepository<UserGiftCard,Long> {
    List<UserGiftCard> findAllByReceivedById(Long receivedByID);
    List<UserGiftCard> findAllByActiveTrueAndGiftCardIdIn(List<Long> giftCardIds);
    List<UserGiftCard> findAllByActiveTrue();

    List<UserGiftCard> findAllBySendById(Long sendByID);

}
