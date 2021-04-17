package com.skrest.crud.dao;

import com.skrest.crud.model.ProductEntity;
import com.skrest.crud.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for handling all CRUD Operations.
 * @author Santosh Kumar
 * @version 1.0
 */
@Repository
public class ProductDAOImpl implements ProductDAO{

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDAOImpl.class);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CassandraOperations cassandraTemplate; // similar to ProductRepository

    @Autowired
    private CqlTemplate cqlTemplate;

    public  ProductDAOImpl(){
        LOGGER.info("Entering ProductDAOImpl...");
    }

    @Override
    public void addProduct(ProductEntity productEntity) {
        productEntity.setId(getNextId());
        productRepository.insert(productEntity);
    }

    private long getNextId(){
        return cqlTemplate.queryForObject("SELECT MAX(id) FROM product", Long.class) + 1;
    }

    @Override
    public long getProductsCount(){
        return cqlTemplate.queryForObject("SELECT COUNT(*) FROM product", Long.class);
    }

    @Override
    public long getProductsCountWithCurrency(String currencyCode){
        return cqlTemplate.queryForObject("SELECT COUNT(*) FROM product WHERE currency_code = ? ALLOW FILTERING", Long.class, currencyCode);
    }

    @Override
    public void addProducts(List<ProductEntity> productEntityList) {
        long id = getNextId();
        for (ProductEntity productEntity : productEntityList) {
            productEntity.setId(id++);
        }
        productRepository.insert(productEntityList);
    }

    @Override
    public ProductEntity getProductById(Long productId) {
        ProductEntity product = new ProductEntity();
        Optional<ProductEntity> productEntity = productRepository.findById(productId);
        if(productEntity.isPresent()){
            product.setName(productEntity.get().getName());
            product.setPrice(productEntity.get().getPrice());
            product.setId(productEntity.get().getId());
            product.setDescription((productEntity.get().getDescription()));
            product.setCurrency_code(productEntity.get().getCurrency_code());
            return product;
        }
        return null;
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }


    @Override
    public List<ProductEntity> getProductByIds(List<Long> productIds) {
        return productRepository.findAllById(productIds);
    }

    @Override
    public ProductEntity updateProductById(Long productId, ProductEntity productEntity) {
        ProductEntity tempProduct = getProductById(productId);
        if(tempProduct != null){
            if(productEntity.getName() != null){
                tempProduct.setName(productEntity.getName());
            }
            if(productEntity.getPrice() != null){
                tempProduct.setPrice(productEntity.getPrice());
            }
            if(productEntity.getCurrency_code() != null){
                tempProduct.setCurrency_code(productEntity.getCurrency_code());
            }
            if(productEntity.getDescription() != null){
                tempProduct.setDescription(productEntity.getDescription());
            }
            productRepository.insert(tempProduct);
            return tempProduct;
        }
        return null;
    }

    @Override
    public List<ProductEntity> updateProducts(List<ProductEntity> productEntity) {
        List<ProductEntity> validProducts = new ArrayList<>();
        for(int i = 0; i < productEntity.size(); i++){
            Long productId = productEntity.get(i).getId();
            if(productId == null || productId < 0){
                // TODO do we want to throw an exception here? Like Id can't be null Bad Request and ask user to resend correct request
                continue;
            }
            ProductEntity tempProduct = updateProductById(productEntity.get(i).getId(), productEntity.get(i));
            if(tempProduct != null){
                validProducts.add(tempProduct);
            }
        }
        return validProducts.size() == 0? null: validProducts;
    }

    @Override
    public void deleteProduct(Long productId) {
        // TODO do we need to write try-catch block here??
        productRepository.deleteById(productId);
    }

    @Override
    public void deleteProducts(List<Long> productIds) {
        // TODO can write a validate id or valid product helper method.
        List<ProductEntity> productEntityList = new ArrayList<>();
        for(int i = 0; i < productIds.size(); i++){
            ProductEntity productEntity = getProductById(productIds.get(i));
            if(productEntity == null){
                String errMsg = String.format("Product Id = %d doesn't exist.", productIds.get(i));
                LOGGER.error(errMsg);
                throw new ResourceNotFoundException(errMsg);
            }
            productEntityList.add(productEntity);
        }
        productRepository.deleteAll(productEntityList);
    }

}
