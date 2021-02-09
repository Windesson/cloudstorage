package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public Integer add(FileForm fileForm) {
        return fileMapper.insert(fileForm);
    }

    public List<FileForm> getFiles(Integer userId) {
        //List<FileForm> fileForms = fileMapper.getFiles(userId);

        return fileMapper.getFiles(userId);
    }
}
