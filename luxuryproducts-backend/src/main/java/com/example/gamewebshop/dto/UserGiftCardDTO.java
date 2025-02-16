package com.example.gamewebshop.dto;


import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.GiftCard;

public class UserGiftCardDTO {
    private Long id;
    private CustomUser sendBy;
    private CustomUser receivedBy;
    private GiftCard giftCard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomUser getSendBy() {
        return sendBy;
    }

    public void setSendBy(CustomUser sendBy) {
        this.sendBy = sendBy;
    }

    public CustomUser getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(CustomUser receivedBy) {
        this.receivedBy = receivedBy;
    }

    public GiftCard getGiftCard() {
        return giftCard;
    }

    public void setGiftCard(GiftCard giftCard) {
        this.giftCard = giftCard;
    }
}
