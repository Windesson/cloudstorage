package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.FileModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<FileModel> getFiles(Integer userId) {
        return fileMapper.getFiles(userId);
    }

    public Integer add(MultipartFile file, Integer userId) throws IOException {
        return fileMapper.insert(new FileModel(file, userId));
    }

    public Integer delete(Integer fileId, Integer userId) {
        return fileMapper.delete(fileId,userId);
    }

    public FileModel get(Integer fileId, Integer userId) {
        return fileMapper.getFile(fileId, userId);
    }
}

