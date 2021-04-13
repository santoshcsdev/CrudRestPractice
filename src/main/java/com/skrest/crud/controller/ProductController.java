package com.skrest.crud.controller;

import com.skrest.crud.dao.ProductDAOImpl;
import com.skrest.crud.entity.ProductEntity;
import com.skrest.crud.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.Nonnegative;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.NoSuchElementException;

@RestController
@Validated
@PropertySource(value = { "classpath:validation_message.properties" })
public class ProductController {

    @Autowired
    private ProductDAOImpl productDAOImpl;

    @Autowired
    private Environment environment;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    private static final String PRODUCT_NOT_NULL = "product-id.notnull";
    private static final String PRODUCT_NON_NEGATIVE = "product-id.non-negative";
    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @GetMapping(value = "/product/{productId}", produces = ("application/json; charset=utf-8"))
    public Product getProductInfo(@PathVariable("productId") @Min(value = 1,message = "wtf") Long productId) throws NoSuchElementException {
        if(productId < 0){
            throw new IllegalArgumentException(environment.getProperty(PRODUCT_NON_NEGATIVE) + productId);
        }

        Product product = productDAOImpl.getProductById(productId);
        if(product == null){
            throw new NoSuchElementException(environment.getProperty(PRODUCT_NOT_NULL) + productId);
        }

        return product;
    }

    @PostMapping (value = "/product", consumes = ("application/json; charset=utf-8"),produces = ("application/json; charset=utf-8"))
    public String addProduct(@RequestBody ProductEntity productEntity) throws ResourceNotFoundException {
        if(productEntity == null){
            LOGGER.error("Product cannot be null");
            // TODO put the string values in resource file
            throw new ResourceNotFoundException("Product Entity cannot be null or empty.");
        }
        else if(productEntity.getName() == null || productEntity.getName().isEmpty()){
            throw new ResourceNotFoundException("Product name cannot be null or empty.");
        }
        else if(productEntity.getPrice() < 0){
            throw new ResourceNotFoundException("Product price cannot be negative.");
        }
        else if(productEntity.getCurrency_code() == null || productEntity.getCurrency_code().isEmpty()){
            throw new ResourceNotFoundException("Product currency code cannot be null or empty.");
        }
        else {
            try {
                if(productDAOImpl.addProduct(productEntity)){
                    return SUCCESS;
                }
                return FAILURE;
            }
            catch (Exception e){
                LOGGER.error("Unable to add the product." +e.getMessage());
                throw new ResourceAccessException("Unable to add the product.");
            }
        }
    }

    @GetMapping(value = "/product/count", produces = ("application/json; charset=utf-8"))
    public Long getProductsCount(){
        return productDAOImpl.getProductsCount();
    }

    @GetMapping(value = "/product/currency-count/{currency_code}", produces = ("application/json; charset=utf-8"))
    public Long getProductsCountWithCurrency(@PathVariable("currency_code") String currencyCode) throws ResourceNotFoundException{
        if(currencyCode == null || currencyCode.trim().isEmpty()){
            throw new ResourceNotFoundException("Currency code cannot be null or empty.");
        }
        return productDAOImpl.getProductsCountWithCurrency(currencyCode);
    }
}
