package com.example.gamewebshop.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class PlacedOrder {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String infix;
    @Column(nullable = true)
    private String last_name;
    @Column(nullable = true)
    private String zipcode;
    @Column(nullable = true)
    private int houseNumber;
    @Column(nullable = true)
    private String notes;
    @Column(nullable = true)
    private int totalProducts;
    @Column(nullable = true)
    private LocalDateTime orderDate;
    @Column(nullable = true)
    private double totalPrice;
    @Column(nullable = true)
    private double discountedPrice;
    @Column(nullable = true)
    private String promoCode;
    @ManyToOne()
    @JsonManagedReference
    private CustomUser user;
    @ManyToMany()
    @JsonManagedReference

    private  Set<ProductOrder> products;
    @ManyToMany
    @JoinTable(
            name = "order_gift_cards",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_card_id")
    )
    @JsonManagedReference
    private Set<GiftCard> giftCards = new HashSet<>();

    private Double totalAmount;
    private Double paidAmountByGiftCard;
    @Transient
    private List<Long> giftCardIds = new ArrayList<>();

    public PlacedOrder() {
    }

    public PlacedOrder(String name, String infix, String last_name, String zipcode, int houseNumber, String notes, int totalProducts, LocalDateTime orderDate, double totalPrice, double discountedPrice, String promocode, CustomUser user, Set<ProductOrder> products) {
        this.name = name;
        this.infix = infix;
        this.last_name = last_name;
        this.zipcode = zipcode;
        this.houseNumber = houseNumber;
        this.notes = notes;
        this.totalProducts = totalProducts;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.discountedPrice = discountedPrice;
        this.promoCode = promocode;
        this.user = user;
        this.products = products;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfix() {
        return infix;
    }

    public void setInfix(String infix) {
        this.infix = infix;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


    public Number getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public CustomUser getUser() {
        return user;
    }

    public void setUser(CustomUser user) {
        this.user = user;
    }

    public Set<ProductOrder> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductOrder> products) {
        this.products = products;
    }
    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }


    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPaidAmountByGiftCard() {
        return paidAmountByGiftCard;
    }

    public void setPaidAmountByGiftCard(Double paidAmountByGiftCard) {
        this.paidAmountByGiftCard = paidAmountByGiftCard;
    }

    public List<Long> getGiftCardIds() {
        return giftCardIds;
    }

    public void setGiftCardIds(List<Long> giftCardIds) {
        this.giftCardIds = giftCardIds;
    }

    public Set<GiftCard> getGiftCards() {
        return giftCards;
    }

    public void setGiftCards(Set<GiftCard> giftCards) {
        this.giftCards = giftCards;
    }

}


