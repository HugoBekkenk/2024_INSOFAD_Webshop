package com.example.gamewebshop.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ProductOrderDTO {
    @JsonAlias("product_id")
    public long productId;
    public VariantOrderDTO[] variantOrders;
    public String type;

    public ProductOrderDTO(long productId, VariantOrderDTO[] variantOrders, String type) {
        this.productId = productId;
        this.variantOrders = variantOrders;
        this.type = type;
    }
}
