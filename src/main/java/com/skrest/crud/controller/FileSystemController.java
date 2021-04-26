package com.skrest.crud.controller;

import com.skrest.crud.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("file-system")
@PropertySource(value = { "classpath:validation_message.properties" })
public class FileSystemController {

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private Environment environment;

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = ("application/json; charset=utf-8"))
    public ResponseEntity<Long> uploadFile(@RequestPart(value = "filename") MultipartFile multipartFile){
        if(multipartFile == null){
            // TODO change later
            throw new IllegalArgumentException("file can't be null");
        }
        Long fileIdentifier;
        try{
            fileIdentifier = fileSystemService.saveFile(multipartFile.getBytes(), multipartFile.getOriginalFilename());
        }
        catch (Exception e){
            throw new ResourceNotFoundException("Error uploading file.");
        }

        return new ResponseEntity<>(fileIdentifier, HttpStatus.OK);
    }


    // public ResponseEntity<FileSystemResource> downloadFile(@PathVariable("fileId") Long fileId)
    @GetMapping(value = "/file/{fileId}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<FileSystemResource> downloadFile(@PathVariable("fileId") Long fileId) {
        if(fileId == null || fileId < 0){
            // TODO change later
            throw new IllegalArgumentException("File Identifier can't be null");
        }
        FileSystemResource fileSystemResource;
        try {
            fileSystemResource = fileSystemService.findFile(fileId);

            // Saving the file to a desired location.
            // TODO mention where the file is saved and the filename too...
            fileSystemService.saveToDownloads(fileSystemResource.getInputStream(), fileSystemResource.getFilename());
        }
        catch (Exception e){
            throw new ResourceNotFoundException("Error downloading file.");
        }
        return new ResponseEntity<>(fileSystemResource, HttpStatus.OK);
    }

}
