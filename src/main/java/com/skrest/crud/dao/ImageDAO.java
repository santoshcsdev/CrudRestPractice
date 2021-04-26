package com.skrest.crud.dao;

import com.skrest.crud.model.ImageEntity;
import org.springframework.core.io.Resource;

import java.nio.ByteBuffer;

public interface ImageDAO {

    Long uploadImage(ImageEntity imageEntity);

    ByteBuffer downloadImage(Long imageIdentifier);

}
