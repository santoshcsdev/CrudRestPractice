package com.skrest.crud.dao;

import com.skrest.crud.entity.ProductEntity;
import com.skrest.crud.model.Product;

import java.util.List;

/**
 * DAO interface for Product to perform CRUD operation.
 * @author Santosh Kumar
 * @version 1.0
 */
public interface ProductDAO {

    public boolean addProduct(ProductEntity productEntity);

    public boolean addProducts(List<ProductEntity> productEntity);

    public Product getProductById(Long productId);

    public List<Product> getProductByIds(List<Long> productId);

    public boolean updateProduct(ProductEntity productEntity);

    public boolean updateProducts(List<ProductEntity> productEntity);

    public boolean deleteProduct(Long productId);

    public boolean deleteProducts(List<Long> productId);

    public long getProductsCount();

    public long getProductsCountWithCurrency(String currencyCode);

    public List<Product> getAllProducts();

}
