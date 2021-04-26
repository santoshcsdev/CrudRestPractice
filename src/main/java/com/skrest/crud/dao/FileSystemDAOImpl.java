package com.skrest.crud.dao;

import com.skrest.crud.model.FileEntity;
import com.skrest.crud.repository.FileSystemDBRepository;
import com.skrest.crud.util.CrudUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public class FileSystemDAOImpl implements FileSystemDAO{

    @Autowired
    private FileSystemDBRepository fileSystemDBRepository;

    @Autowired
    private CqlTemplate cqlTemplate;

    @Override
    public Long saveFile(FileEntity fileEntity) {
        fileEntity.setId(getNextId());
        return fileSystemDBRepository.save(fileEntity).getId();
    }

    @Override
    public FileEntity findFile(Long fileId) {
        return fileSystemDBRepository.findById(fileId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private long getNextId(){
        try{
            return cqlTemplate.queryForObject("SELECT MAX(id) FROM file", Long.class) + 1;
        }
        catch (NullPointerException ignored){
        }
        return 1;
    }
}
