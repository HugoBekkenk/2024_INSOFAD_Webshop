package com.example.gamewebshop.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class ProductOrder {
    @Id
    @GeneratedValue
    private long id;

    private long productId;
    private String name;
    @Column(length = 500)
    private String description;
    private Number price;
    private String imgURL;
    @Column(length = 500)
    private String specifications;
    private String releaseDate;
    private String publisher;
    private String categoryName;
    private String type;

    @ManyToMany()
    @JsonManagedReference
    private Set<VariantOrder> variants;

    public ProductOrder() {
    }

    public ProductOrder(long productId, String name, String description, Number price, String imgURL, String specifications, String releaseDate, String publisher, Category category, Set<VariantOrder> variants, String type) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgURL = imgURL;
        this.specifications = specifications;
        this.releaseDate = releaseDate;
        this.publisher = publisher;
        this.categoryName = category != null ? category.getName() : null;
        this.variants = variants;
        this.type = type;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
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

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<VariantOrder> getVariants() {
        return variants;
    }

    public void setVariants(Set<VariantOrder> variants) {
        this.variants = variants;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
