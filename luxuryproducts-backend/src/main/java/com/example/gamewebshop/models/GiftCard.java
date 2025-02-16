package com.example.gamewebshop.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "gift_card")
public class GiftCard {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Double amount;
    private String image;
    @JsonIgnore
    private boolean active;

    private String cardCode;

    public GiftCard() {
    }

    public GiftCard(String name, Double amount, String image, boolean active) {
        this.name = name;
        this.amount = amount;
        this.image = image;
        this.active = active;
    }

    public GiftCard(Long id, String name, Double amount, String image, boolean active) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.image = image;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    @Override
    public String toString() {
        return "GiftCard{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", image='" + image + '\'' +
                ", active=" + active +
                ", cardCode='" + cardCode + '\'' +
                '}';
    }
}
