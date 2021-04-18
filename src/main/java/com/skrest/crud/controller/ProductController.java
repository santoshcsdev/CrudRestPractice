package com.skrest.crud.controller;

import com.skrest.crud.ProductDAOImpl;
import com.skrest.crud.model.ProductEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
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
    private static final String PRODUCT_NON_NULL_OR_NEGATIVE = "product-id.non-negative";
    private static final String PRODUCT_NOT_FOUND = "product.not-found";
    private static final String PRODUCT_LIST_NOT_FOUND = "product-list-notnull";
    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @GetMapping(value = "/product/{productId}", produces = ("application/json; charset=utf-8"))
    public ResponseEntity<ProductEntity>  getProductInfo(@PathVariable("productId") @Min(value = 1,message = "wtf") Long productId){
        if(productId == null || productId < 0){
            throw new IllegalArgumentException(environment.getProperty(PRODUCT_NON_NULL_OR_NEGATIVE) + productId);
        }
        ProductEntity product = null;
        try {
            product = productDAOImpl.getProductById(productId);
            if(product == null){
                throw new NoSuchElementException(environment.getProperty(PRODUCT_NOT_FOUND) + productId);
            }
        }
        catch (Exception e){
            throw new IllegalArgumentException(String.format("Error in retrieving the product: %s", e.getMessage()));
        }
        return new ResponseEntity<ProductEntity>(product, HttpStatus.OK);
    }

    @GetMapping(value = "/products/", produces = ("application/json; charset=utf-8"))
    public ResponseEntity<List<ProductEntity>> getAllProducts(){
        List<ProductEntity> products = null;
        try {
            products = productDAOImpl.getAllProducts();
        }
        catch (Exception e){
            throw new IllegalArgumentException(String.format("Error in retrieving the products: %s", e.getMessage()));
        }
        return new ResponseEntity<List<ProductEntity>>(products, HttpStatus.OK);
    }

    @PutMapping(value = "/product/{productId}", consumes = ("application/json; charset=utf-8"), produces = ("application/json; charset=utf-8"))
    public ResponseEntity<ProductEntity> updateProductById(@PathVariable("productId") Long productId, @RequestBody ProductEntity productEntity){
        if(productId == null || productId < 0){
            throw new IllegalArgumentException(environment.getProperty(PRODUCT_NON_NULL_OR_NEGATIVE) + productId);
        }
        try {
            productEntity = productDAOImpl.updateProductById(productId, productEntity);
        }
        catch (Exception e){
            throw new IllegalArgumentException(String.format("Error in updating the  product: %s", e.getMessage()));
        }
        return new ResponseEntity<>(productEntity, HttpStatus.OK);
    }

    @PutMapping(value = "/product", consumes = ("application/json; charset=utf-8"), produces = ("application/json; charset=utf-8"))
    public ResponseEntity<ProductEntity> updateProduct(@RequestBody ProductEntity productEntity){
        if(productEntity.getId() == null || productEntity.getId() < 0){
            throw new IllegalArgumentException(environment.getProperty(PRODUCT_NON_NULL_OR_NEGATIVE) + productEntity.getId());
        }
        try {
            productEntity = productDAOImpl.updateProductById(productEntity.getId(),productEntity);
        }
        catch (Exception e){
            throw new IllegalArgumentException(String.format("Error in updating the  product: %s", e.getMessage()));
        }
        return new ResponseEntity<>(productEntity, HttpStatus.OK);
    }

    @PutMapping(value = "/products", consumes = ("application/json; charset=utf-8"), produces = ("application/json; charset=utf-8"))
    public ResponseEntity<List<ProductEntity>> updateProducts(@RequestBody List<ProductEntity> productEntityList){
        if(productEntityList == null || productEntityList.size() == 0){
            throw new IllegalArgumentException(environment.getProperty(PRODUCT_LIST_NOT_FOUND));
        }
        try {
            productEntityList = productDAOImpl.updateProducts(productEntityList);
        }
        catch (Exception e){
            throw new IllegalArgumentException(String.format("Error in updating the  products: %s", e.getMessage()));
        }
        return new ResponseEntity<>(productEntityList, HttpStatus.OK);
    }

    @PostMapping(value = "/products/ids", consumes = ("application/json; charset=utf-8"), produces = ("application/json; charset=utf-8"))
    public ResponseEntity<List<ProductEntity>> getProductsWithIds(@RequestBody List<Long> productIds){
        List<ProductEntity> products = null;
        try{
            products = productDAOImpl.getProductByIds(productIds);
        }
        catch (Exception e){
            throw new IllegalArgumentException(String.format("Error in retrieving the  products: %s", e.getMessage()));
        }
        return new ResponseEntity<List<ProductEntity>>(products, HttpStatus.OK);
    }

    @PostMapping (value = "/product", consumes = ("application/json; charset=utf-8"), produces = ("application/json; charset=utf-8"))
    public ResponseEntity<String> addProduct(@RequestBody ProductEntity productEntity) throws ResourceNotFoundException {
        checkProductEntity(productEntity);
        try {
            // TODO do not accept id as input in request body -- handle that case...
            if(!productDAOImpl.addProduct(productEntity)){
                throw new ResourceNotFoundException("Unable to add product.");
            }
        }
        catch (Exception e){
            throw new IllegalArgumentException(String.format("Error in adding the product: %s", e.getMessage()));
        }
        return new ResponseEntity<String>(String.format("Product added with Id: %s", String.valueOf(productEntity.getId())), HttpStatus.CREATED);
    }

    private void checkProductEntity(ProductEntity productEntity){
        if(productEntity == null){
            throw new ResourceNotFoundException("Product Entity cannot be null or empty.");
        }
        else if(productEntity.getName() == null || productEntity.getName().isEmpty()){
            throw new ResourceNotFoundException("Product name cannot be null or empty.");
        }
        else if(productEntity.getPrice() == null || productEntity.getPrice() < 0){
            throw new ResourceNotFoundException("Product price cannot be null or negative.");
        }
        else if(productEntity.getCurrency_code() == null || productEntity.getCurrency_code().isEmpty()){
            throw new ResourceNotFoundException("Product currency code cannot be null or empty.");
        }
    }

    @PostMapping(value = "/products", consumes = ("application/json; charset=utf-8"), produces = ("application/json; charset=utf-8"))
    public String addProducts(@RequestBody List<ProductEntity> productEntityList) throws ResourceNotFoundException{
        if(productEntityList == null || productEntityList.size() == 0){
            throw new ResourceNotFoundException("Product List cannot be null or empty.");
        }
        for (ProductEntity productEntity : productEntityList) {
            checkProductEntity(productEntity);
        }
        try{
            productDAOImpl.addProducts(productEntityList);
            return SUCCESS;
        }
        catch (Exception e){
            throw new IllegalArgumentException(String.format("Error in adding the products: %s", e.getMessage()));
        }
    }

    @DeleteMapping(value = "/product/{productId}", produces = ("application/json; charset=utf-8"))
    public ResponseEntity<String> deleteProductById(@PathVariable("productId") Long productId){
        if(productId == null || productId < 0){
            throw new IllegalArgumentException(environment.getProperty(PRODUCT_NON_NULL_OR_NEGATIVE));
        }
        try {
            if(productDAOImpl.getProductById(productId) == null){
                throw new IllegalArgumentException(String.format("No product with id: %d exists.", productId));
            }
            productDAOImpl.deleteProductById(productId);
        }
        catch (Exception e){
            throw new IllegalArgumentException(String.format("Error in deleting the product with id: %d. %s", productId, e.getMessage()));
        }
        return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping(value = "/product", consumes = ("application/json; charset=utf-8"), produces = ("application/json; charset=utf-8"))
    public ResponseEntity<String> deleteProduct(@RequestBody ProductEntity productEntity){
        if(productEntity == null){
            throw new IllegalArgumentException(environment.getProperty(PRODUCT_NOT_NULL));
        }
        if(productEntity.getId() == null || productEntity.getId() < 0){
            throw new IllegalArgumentException(environment.getProperty(PRODUCT_NON_NULL_OR_NEGATIVE) + productEntity.getId());
        }
        if(productDAOImpl.getProductById(productEntity.getId()) == null){
            throw new NoSuchElementException(environment.getProperty(PRODUCT_NOT_FOUND) + productEntity.getId());
        }
        try{
            productDAOImpl.deleteProduct(productEntity);
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Product doesn't exist.", e);
        }
        catch (Exception e){
            throw new IllegalArgumentException(String.format("Error in deleting the product: %s", e.getMessage()));
        }

        return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping(value = "/products", consumes = ("application/json; charset=utf-8"), produces = ("application/json; charset=utf-8"))
    public ResponseEntity<String> deleteProductsById(@RequestBody List<Long> productIds){
        if(productIds == null || productIds.size() == 0){
            throw new IllegalArgumentException(environment.getProperty(PRODUCT_LIST_NOT_FOUND));
        }
        try{
            productDAOImpl.deleteProductsById(productIds);
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Product Id doesn't exist.", e);
        }
        catch (Exception e){
            throw new IllegalArgumentException(String.format("Error in deleting the products: %s", e.getMessage()));
        }

        return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
    }

    @GetMapping(value = "/product/count", produces = ("application/json; charset=utf-8"))
    public Long getProductsCount(){
        return productDAOImpl.getProductsCount();
    }

    @GetMapping(value = "/product/currency-count/{currency_code}", produces = ("application/json; charset=utf-8"))
    public Long getProductsCountWithCurrency(@PathVariable("currency_code") String currencyCode) throws ResourceNotFoundException{
        if(currencyCode == null || currencyCode.trim().isEmpty()){
            throw new IllegalArgumentException("Currency code cannot be null or empty.");
        }
        return productDAOImpl.getProductsCountWithCurrency(currencyCode);
    }
}
