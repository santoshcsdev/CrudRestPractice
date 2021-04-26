package com.skrest.crud.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.nio.ByteBuffer;

@Setter
@Getter
@Table(value = "image")
public class ImageEntity {

    @PrimaryKey
    public Long id;

    @Column
    ByteBuffer content;

    @Column
    String name;

    //@CassandraType(type = CassandraType.Name.BLOB) ByteBuffer data;

    public ImageEntity(){

    }

    public ImageEntity(Long id, ByteBuffer content, String name){
        this.id = id;
        this.content = content;
        this.name = name;
    }
}
