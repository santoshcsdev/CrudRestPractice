package com.skrest.crud.util;

import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Utility class for handling all CRUD Operations.
 * @author Santosh Kumar
 * @version 1.0
 */
@Repository
public class ProductOperations {

    @Autowired
    private CassandraTemplate cassandraTemplate;

    private CqlTemplate cqlTemplate;

    public <T> T addProduct(T entity){
        return cassandraTemplate.insert(entity);
    }

    public <T> void  addProducts(List<T> entities){
        cassandraTemplate.insert(entities);
    }

    public void addProducts(){

    }

}
