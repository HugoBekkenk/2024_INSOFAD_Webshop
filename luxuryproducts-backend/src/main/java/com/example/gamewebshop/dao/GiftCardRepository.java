package com.example.gamewebshop.dao;

import com.example.gamewebshop.models.GiftCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GiftCardRepository extends JpaRepository<GiftCard,Long> {
    List<GiftCard> findAllByActiveTrue();
    Optional<GiftCard> findByIdAndActiveTrue(Long id);
    List<GiftCard> findAllByActiveTrueAndIdIn(List<Long> giftCardIds);
    Optional<GiftCard> findGiftCardByName(String name);
}
