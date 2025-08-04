package com.monolithicbank.product.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@NoArgsConstructor
@Data
public class Product {

    private String id;
    private String name;
    private String description;
    private double interestRate;
    private String currency;
    private String createdDate;
    private String updatedDate;

    @Builder
    public Product(String id, String name, String description, double interestRate, String currency, String createdDate, String updatedDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.interestRate = interestRate;
        this.currency = currency;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}