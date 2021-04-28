package com.skrest.crud.controller;

import com.skrest.crud.service.FileSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("file-system")
@PropertySource(value = { "classpath:validation_message.properties" })
public class FileSystemController {

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private Environment environment;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemController.class);


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
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("fileId") Long fileId) {
        if(fileId == null || fileId < 0){
            // TODO change later
            throw new IllegalArgumentException("File Identifier can't be null");
        }
        FileSystemResource fileSystemResource;
        ResponseEntity<InputStreamResource> responseEntity = null;
        try {
            fileSystemResource = fileSystemService.findFile(fileId);

            String filename = fileSystemResource.getFilename();
            if(filename == null || filename.isEmpty()){
                throw new ResourceNotFoundException("File name is null");
            } 

            String mimeType = getFileExtension(filename);
            if(mimeType != null && !mimeType.isEmpty()){
                HttpHeaders headers = new HttpHeaders();

                headers.add("Access-Control-Allow-Origin", "*");
                headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
                headers.add("Access-Control-Allow-Headers", "Content-Type");
                //headers.add("Content-Disposition", "filename=" + filename);
                headers.add("Content-Disposition", "inline; filename=" + filename);
                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");
                headers.setContentLength(fileSystemResource.contentLength());
                headers.setContentType(MediaType.parseMediaType(mimeType));

                responseEntity = new ResponseEntity<InputStreamResource>
                        (new InputStreamResource(fileSystemResource.getInputStream()), headers, HttpStatus.OK);
            }
            
            // Saving the file to a desired location.
            // TODO mention where the file is saved and the filename too...
            //fileSystemService.saveToDownloads(fileSystemResource.getInputStream(), fileSystemResource.getFilename());
        }
        catch (Exception e){
            throw new ResourceNotFoundException("Error downloading file.");
        }

        return responseEntity;
    }

    private String getFileExtension(String filename){
        MediaType mediaType = null;
        if(filename == null || filename.isEmpty()){
            return null;
        }
        Path path = new File(filename).toPath();
        String mimeType = "";
        try{
            mimeType = Files.probeContentType(path);
        }
        catch (IOException e){
            LOGGER.error(String.format("Unable to retrieve file extension, exception: %s", e.getMessage()));
        }

        return mimeType;
    }

}
