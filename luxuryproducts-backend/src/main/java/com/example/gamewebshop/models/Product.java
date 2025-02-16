package com.example.gamewebshop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    @Column(length = 500)
    private String description;
    private Number price;
    private String imgURL;
    @Column(length = 500)
    private String specifications;
    private String releaseDate;
    private String publisher;
    private Number stock;
    private String type;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private Set<Variant> variants;

    public Product() {
    }

    public Product(String name, String description, Number price, String imgURL, Category category, String specifications, String releaseDate, String publisher, String type) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgURL = imgURL;
        this.specifications = specifications;
        this.releaseDate = releaseDate;
        this.publisher = publisher;
        this.category = category;
        this.stock = 20;
        this.type = type;
    }

    public Product(String name, String description, Number price, String imgURL, String specifications, String releaseDate, String publisher, Number stock, Category category, String type) {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public Set<Variant> getVariants() {
        return variants;
    }

    public void setVariants(Set<Variant> variants) {
        this.variants = variants;
    }

    public Number getStock() {
        return stock;
    }

    public void setStock(Number stock) {
        this.stock = stock;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imgURL='" + imgURL + '\'' +
                ", specifications='" + specifications + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", publisher='" + publisher + '\'' +
                ", stock=" + stock +
                ", type='" + type + '\'' +
                ", category=" + category +
                ", variants=" + variants +
                '}';
    }
}
