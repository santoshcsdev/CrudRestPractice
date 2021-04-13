package com.skrest.crud.repository;

import com.skrest.crud.entity.ProductEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface ProductRepository extends CassandraRepository<ProductEntity, Long>{
}
