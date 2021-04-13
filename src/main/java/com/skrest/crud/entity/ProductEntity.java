package com.skrest.crud.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.Min;

@Setter
@Getter
@Table(value = "product")
public class ProductEntity {
    @PrimaryKey
    private Long id;

    @Column
    private String name;

    @Column
    private Double price;

    @Column
    private String currency_code;

    @Column
    private String description;

    public ProductEntity(Long id, String name, Double price, String currency_code, String description){
        this.id = id;
        this.name = name;
        this.price = price;
        this.currency_code = currency_code;
        this.description = description;
    }

    @Override
    public String toString(){
        return String.format("{ @type = %1$s, id = %2$s, name = %3$s, price = %4$s, currency_code = %5$s,  description = %6$s}",
                getClass().getName(), getId(), getName(), getPrice(), getCurrency_code(), getDescription());
    }
}
