package com.skrest.crud.controller;

import com.skrest.crud.dao.ImageDAOImpl;
import com.skrest.crud.model.ImageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.ByteBuffer;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

    @Autowired
    private ImageDAOImpl imageDAOImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = ("application/json; charset=utf-8"))
    public ResponseEntity<Long> uploadImage(@RequestPart(value = "image") MultipartFile multipartFile) {
        if(multipartFile == null){
            // TODO change later
            throw new IllegalArgumentException("file can't be null");
        }
        ImageEntity imageEntity = new ImageEntity();
        Long imageIdentifier;
        try{
            imageEntity.setName(multipartFile.getName());
            imageEntity.setContent(ByteBuffer.wrap(multipartFile.getBytes()));
            imageIdentifier = imageDAOImpl.uploadImage(imageEntity);
        }
        catch (Exception e){
            throw new ResourceNotFoundException("Error uploading image file.");
        }
        return new ResponseEntity<>(imageIdentifier, HttpStatus.OK);
    }

    @GetMapping(value = "/image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> downloadImage(@PathVariable("imageId") Long imageId){
        if(imageId == null || imageId < 0){
            // TODO change later
            throw new IllegalArgumentException("Image Identifier can't be null");
        }
        ByteBuffer image;
        Resource imgResource;
        try {
            image = imageDAOImpl.downloadImage(imageId);
            imgResource = new ByteArrayResource(image.array());
        }
        catch (Exception e){
            throw new ResourceNotFoundException("Error downloading image file.");
        }

        return new ResponseEntity<>(imgResource, HttpStatus.OK);
    }
}
