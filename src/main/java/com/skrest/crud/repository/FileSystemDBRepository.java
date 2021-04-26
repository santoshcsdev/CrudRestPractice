package com.skrest.crud.repository;

import com.skrest.crud.model.FileEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface FileSystemDBRepository extends CassandraRepository<FileEntity, Long> {
}
