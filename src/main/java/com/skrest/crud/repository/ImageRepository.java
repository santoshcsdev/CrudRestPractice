package com.skrest.crud.repository;

import com.skrest.crud.model.ImageEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface ImageRepository extends CassandraRepository<ImageEntity, Long> {

}
