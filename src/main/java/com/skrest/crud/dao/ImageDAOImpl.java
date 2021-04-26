package com.skrest.crud.dao;

import com.skrest.crud.model.ImageEntity;
import com.skrest.crud.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.nio.ByteBuffer;

@Repository
public class ImageDAOImpl implements ImageDAO{

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDAOImpl.class);

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CqlTemplate cqlTemplate;

    @Override
    public Long uploadImage(ImageEntity imageEntity) {
        imageEntity.setId(getNextId());
        return imageRepository.save(imageEntity).getId();
    }

    @Override
    public ByteBuffer downloadImage(Long imageIdentifier) {

        ByteBuffer byteBuffer = imageRepository.findById(imageIdentifier)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getContent();

        return byteBuffer;
    }

    private long getNextId(){
        try{
            return cqlTemplate.queryForObject("SELECT MAX(id) FROM image", Long.class) + 1;
        }
        catch (NullPointerException ignored){
        }
        return 1;
    }

}
