package com.example.gamewebshop.dto;

public class OptionDTO {
    public String name;
    public Number priceAdded;

    public Long variantId;

    public OptionDTO(String name, Number priceAdded, Long variantId) {
        this.name = name;
        this.priceAdded = priceAdded;
        this.variantId = variantId;
    }
}
