package com.example.gamewebshop.dto;

public class VariantDTO {
    private String name;
    private Long productId;
    private OptionDTO[] options;

    public VariantDTO(String name, OptionDTO[] options, Long productId) {
        this.name = name;
        this.options = options;
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OptionDTO[] getOptions() {
        return options;
    }

    public void setOptions(OptionDTO[] options) {
        this.options = options;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

}
