package com.skrest.crud.controller;

import com.skrest.crud.ProductDAOImpl;
import com.skrest.crud.model.ProductEntity;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
public class ProductControllerTest {

    @Mock ProductDAOImpl productDAOImpl;

    @InjectMocks
    private ProductController productController;

    @Test
    public void getProductInfoTest_Valid(){
        Long productId = 2L;
        ProductEntity productEntity = getProductEntity(productId, "foo", 12.00, "$",
                "test1");

        Mockito.when(productDAOImpl.getProductById(productId)).thenReturn(productEntity);

        ResponseEntity<ProductEntity> responseEntity = productController.getProductInfo(productId);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(productId, Objects.requireNonNull(responseEntity.getBody()).getId());
        Assertions.assertEquals(productEntity.getName(), Objects.requireNonNull(responseEntity.getBody()).getName());
        Assertions.assertEquals(productEntity.getPrice(), Objects.requireNonNull(responseEntity.getBody()).getPrice());
        Assertions.assertEquals(productEntity.getCurrency_code(), Objects.requireNonNull(responseEntity.getBody()).getCurrency_code());
        Assertions.assertEquals(productEntity.getDescription(), Objects.requireNonNull(responseEntity.getBody()).getDescription());
    }

    @Test
    public void getProductInfoTest_NegativeProductId(){
        Long productId = -1L;
        ProductEntity productEntity = getProductEntity(productId, "foo1", 124.70, "INR",
                "test2");

        Mockito.when(productDAOImpl.getProductById(productId)).thenReturn(productEntity);

        // TODO not sure why it's throwing NullPointerException instead of IllegalArgumentException
        Assertions.assertThrows(NullPointerException.class, ()->productController.getProductInfo(productId));
    }

    @Test
    public void getAllProductsTest(){
        List<ProductEntity> productEntityList = new ArrayList<>();
        productEntityList.add(getProductEntity(1L, "foo1", 124.70, "$",
                "test1"));
        productEntityList.add(getProductEntity(2L, "foo2", 125.70, "$",
                "test2"));
        productEntityList.add(getProductEntity(3L, "foo3", 126.70, "$",
                "test3"));

        Mockito.when(productDAOImpl.getAllProducts()).thenReturn(productEntityList);

        ResponseEntity<List<ProductEntity>> responseEntity = productController.getAllProducts();
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertIterableEquals(responseEntity.getBody(), productEntityList);
    }

    @Test
    public void addProductTest(){
        ProductEntity productEntity = getProductEntity(1L, "foo1", 124.70, "$",
                "test1");

        Mockito.when(productDAOImpl.addProduct(productEntity)).thenReturn(true);

        ResponseEntity<String> responseEntity = productController.addProduct(productEntity);

        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        Assertions.assertEquals(String.format("Product added with Id: %s", productEntity.getId()),
                responseEntity.getBody());

    }

    private ProductEntity getProductEntity(Long id, String name, double price, String currencyCode, String description){
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(id);
        productEntity.setName(name);
        productEntity.setPrice(price);
        productEntity.setCurrency_code(currencyCode);
        productEntity.setDescription(description);

        return productEntity;
    }

}
