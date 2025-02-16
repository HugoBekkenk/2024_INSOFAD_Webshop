package com.example.gamewebshop.dto;

import com.example.gamewebshop.models.GiftCard;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    public String name;
    public String infix;
    public String last_name;
    public String zipcode;
    public int houseNumber;
    public String notes;
    @JsonAlias("product_ids")
    public ProductOrderDTO[] productDTOS;
    public String promoCode;
    private Double totalAmount;
    private Double paidAmountByGiftCard;
    private List<Long> giftCardIds = new ArrayList<>();
    private List<GiftCard> giftCards;

    public OrderDTO(){}
    public OrderDTO(String name, String infix, String last_name, String zipcode, int houseNumber, String notes, ProductOrderDTO[] productDTOS, Double totalAmount, Double paidAmountByGiftCard, List<Long> giftCardIds, List<GiftCard> giftCards) {
        this.name = name;
        this.infix = infix;
        this.last_name = last_name;
        this.zipcode = zipcode;
        this.houseNumber = houseNumber;
        this.notes = notes;
        this.productDTOS = productDTOS;
        this.totalAmount = totalAmount;
        this.paidAmountByGiftCard = paidAmountByGiftCard;
        this.giftCardIds = giftCardIds;
        this.giftCards = giftCards;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getInfix() {
        return infix;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public String getNotes() {
        return notes;
    }




    public Double getTotalAmount() {
        return totalAmount;
    }

    public Double getPaidAmountByGiftCard() {
        return paidAmountByGiftCard;
    }

    public List<Long> getGiftCardIds() {
        return giftCardIds;
    }

    public List<GiftCard> getGiftCards() {
        return giftCards;
    }

    public void setGiftCards(List<GiftCard> giftCards) {
        this.giftCards = giftCards;
    }


    public ProductOrderDTO[] getProductDTOS() {
        return productDTOS;
    }

    public void setProductDTOS(ProductOrderDTO[] productDTOS) {
        this.productDTOS = productDTOS;
    }
}
