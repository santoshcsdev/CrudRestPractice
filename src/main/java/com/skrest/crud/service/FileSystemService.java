package com.skrest.crud.service;

import com.skrest.crud.core.StorageException;
import com.skrest.crud.core.StorageFileNotFoundException;
import com.skrest.crud.dao.FileSystemDAOImpl;
import com.skrest.crud.model.FileEntity;
import com.skrest.crud.repository.FileSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemService {

    @Autowired
    FileSystemRepository fileSystemRepository;

    @Autowired
    FileSystemDAOImpl fileSystemDAOImpl;

    private final  Path destinationPath = Paths.get("/Users/santoshkumar/Downloads/crudDownloads");

    public Long saveFile(byte[]bytes,String filename) throws Exception{

        String location = fileSystemRepository.saveFile(bytes,filename);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(filename);
        fileEntity.setLocation(location);

        return fileSystemDAOImpl.saveFile(fileEntity);
    }

    public FileSystemResource findFile(Long fileId){

        FileEntity fileEntity = fileSystemDAOImpl.findFile(fileId);

        return fileSystemRepository.findFile(fileEntity.getLocation());
    }

    // Working method
    public void saveToDownloads(InputStream inputStream, String filename) {
        try {
            Path destinationFile = destinationPath.resolve(
                    Paths.get(filename))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(destinationPath.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    // TODO unsure method
    public Resource loadAsResource(String filename, String location) {
        try {
            Path rootLocation = Paths.get(location);
            Path file = load(filename, rootLocation);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    public Path load(String filename, Path rootLocation) {
        return rootLocation.resolve(filename);
    }
}
