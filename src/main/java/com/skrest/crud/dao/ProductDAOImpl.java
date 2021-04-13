package com.skrest.crud.dao;

import com.skrest.crud.entity.ProductEntity;
import com.skrest.crud.model.Product;
import com.skrest.crud.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public boolean addProduct(ProductEntity productEntity) {
        try{
            long maxId = cqlTemplate.queryForObject("SELECT MAX(id) FROM product", Long.class);
            productEntity.setId(maxId + 1);
            productRepository.insert(productEntity);
        }
        catch (Exception e){
            LOGGER.error("Unable to add product." + e.getMessage());
            throw e;
        }
        return true;
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
    public boolean addProducts(List<ProductEntity> productEntity) {
        return false;
    }

    @Override
    public Product getProductById(Long productId) {
        Product product = new Product();
        Optional<ProductEntity> productEntity = productRepository.findById(productId);
        if(productEntity.isPresent()){
            product.setName(productEntity.get().getName());
            product.setPrice(productEntity.get().getPrice());
            product.setId(productEntity.get().getId());
            product.setDescription((productEntity.get().getDescription()));
            product.setCurrencyCode(productEntity.get().getCurrency_code());
            return product;
        }
        return null;
    }

    @Override
    public List<Product> getProductByIds(List<Long> ids) {
        return null;
    }

    @Override
    public boolean updateProduct(ProductEntity productEntity) {
        return false;
    }

    @Override
    public boolean updateProducts(List<ProductEntity> productEntity) {
        return false;
    }

    @Override
    public boolean deleteProduct(Long id) {
        return false;
    }

    @Override
    public boolean deleteProducts(List<Long> ids) {
        return false;
    }

    @Override
    public List<Product> getAllProducts() {
        return null;
    }
}
