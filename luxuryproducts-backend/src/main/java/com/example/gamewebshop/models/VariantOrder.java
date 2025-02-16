package com.example.gamewebshop.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class VariantOrder {
    @Id
    @GeneratedValue
    private long id;


    private String variantName;
    private String selectedOptionName;
    private Number priceAdded;



    public VariantOrder() {
    }

    public VariantOrder(String variantName, String selectedOptionName, Number priceAdded) {
        this.variantName = variantName;
        this.selectedOptionName = selectedOptionName;
        this.priceAdded = priceAdded;
    }



    public long getId() {
        return id;
    }

    public String getVariantName() {
        return variantName;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getSelectedOptionName() {
        return selectedOptionName;
    }

    public Number getPriceAdded() {
        return priceAdded;
    }

    public void setSelectedOptionName(String selectedOptionName) {
        this.selectedOptionName = selectedOptionName;
    }

    public void setPriceAdded(Number priceAdded) {
        this.priceAdded = priceAdded;
    }
}
