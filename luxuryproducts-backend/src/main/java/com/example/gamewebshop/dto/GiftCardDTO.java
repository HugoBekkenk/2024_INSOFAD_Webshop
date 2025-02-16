package com.example.gamewebshop.dto;

public class GiftCardDTO {
    private Long id;
    private String name;
    private Double amount;
    private String image;

    private String cardCode;

    public GiftCardDTO() {
    }

    public GiftCardDTO(Long id, String name, Double amount, String image) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.image = image;
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

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }
}
