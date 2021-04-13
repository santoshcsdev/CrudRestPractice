package com.skrest.crud.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Product {

    private Long id;

    private String name;

    private Double price;

    private String currencyCode;

    private String description;
}
