package com.example.gamewebshop.models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Options {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private Number priceAdded;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonBackReference
    private Variant variant;

    public Options() {
    }

    public Options(String name, Number priceAdded, Variant variant) {
        this.name = name;
        this.priceAdded = priceAdded;
        this.variant = variant;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Number getPriceAdded() {
        return priceAdded;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPriceAdded(Number priceAdded) {
        this.priceAdded = priceAdded;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }
}
