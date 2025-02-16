package com.example.gamewebshop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Variant {
    @Id
    @GeneratedValue
    private long id;
    private String name;

    @OneToMany(mappedBy = "variant")
    @JsonManagedReference
    private Set<Options> options;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference
    private Product product;

    public Variant() {
    }


    public Variant(String name, Product product) {
        this.name = name;
        this.product = product;
    }


    public long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setId(long id) {
        this.id = id;
    }


    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public Set<Options> getOptions() {
        return options;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOptions(Set<Options> options) {
        this.options = options;
    }
}
