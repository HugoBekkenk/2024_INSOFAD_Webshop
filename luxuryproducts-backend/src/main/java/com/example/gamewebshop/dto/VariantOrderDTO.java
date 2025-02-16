package com.example.gamewebshop.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class VariantOrderDTO {
    @JsonAlias("variant_id")
    public long variantId;
    @JsonAlias("option_id")
    public long selectedOptionId;

    public VariantOrderDTO(long variantId, long selectedOptionId) {
        this.variantId = variantId;
        this.selectedOptionId = selectedOptionId;
    }
}
