package com.skrest.crud.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Setter
@Getter
@Table(value = "file")
public class FileEntity {

    @PrimaryKey
    public Long id;

    @Column
    String filename;

    @Column
    String location;

    public FileEntity(){

    }

    public FileEntity(Long id, String filename, String location){
        this.id = id;
        this.filename = filename;
        this.location = location;
    }
}
