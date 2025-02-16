package com.example.gamewebshop.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity(name="user_gift_card")
public class UserGiftCard {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "send_by")
    private CustomUser sendBy;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "received_by")
    private CustomUser receivedBy;

    @ManyToOne
    @JoinColumn(name = "gift_card_id")
    private GiftCard giftCard;
    @JsonIgnore
    private boolean active;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
