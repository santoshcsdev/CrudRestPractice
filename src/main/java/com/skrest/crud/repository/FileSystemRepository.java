package com.skrest.crud.repository;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Repository
public class FileSystemRepository{

    private final String RESOURCES_DIR = FileSystemRepository.class.getResource("/").getPath();

    public String saveFile(byte[] content, String imageName) throws Exception {

        Path newFile = Paths.get(RESOURCES_DIR + new Date().getTime() + "_" + imageName);
        Files.createDirectories(newFile.getParent());

        Files.write(newFile, content);

        return newFile.toAbsolutePath().toString();
    }

    public FileSystemResource findFile(String location){
        try {
            return new FileSystemResource(Paths.get(location));
        }
        catch (Exception e){
            throw new RuntimeException();
        }
    }
}
