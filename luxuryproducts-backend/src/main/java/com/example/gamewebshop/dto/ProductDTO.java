package com.example.gamewebshop.dto;

public class ProductDTO {
    public String name;
    public String description;
    public Number price;
    public String imgURL;
    public String specifications;
    public String releaseDate;
    public String publisher;
    public Number stock;
    public String category;
    public String type;

    public VariantDTO[] variants;

    public ProductDTO(String name, String description, Number price, String imgURL, String specifications, String releaseDate, String publisher, Number stock, String category, String type, VariantDTO[] variants) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgURL = imgURL;
        this.specifications = specifications;
        this.releaseDate = releaseDate;
        this.publisher = publisher;
        this.stock = stock;
        this.category = category;
        this.type = type;
        this.variants = variants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Number getStock() {
        return stock;
    }

    public void setStock(Number stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public VariantDTO[] getVariants() {
        return variants;
    }

    public void setVariants(VariantDTO[] variants) {
        this.variants = variants;
    }
}
