package com.skrest.crud.dao;

import com.skrest.crud.model.FileEntity;

public interface FileSystemDAO {

    Long saveFile(FileEntity fileEntity);

    FileEntity findFile(Long fileId);

}
