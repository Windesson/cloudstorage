package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
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


    public List<FileForm> getFiles(Integer userId) {
        //List<FileForm> fileForms = fileMapper.getFiles(userId);

        return fileMapper.getFiles(userId);
    }

    public Integer add(MultipartFile file, Integer userId) throws IOException {
        FileForm fileForm = new FileForm(file, userId);
        return fileMapper.insert(fileForm);
    }
}
