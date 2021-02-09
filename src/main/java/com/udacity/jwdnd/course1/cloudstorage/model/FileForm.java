package com.udacity.jwdnd.course1.cloudstorage.model;

public class FileForm {
    private Integer fileId;
    private String filename;
    private String contentType;
    private String filesize;
    private Integer userId;
    private byte[] filedata;

    public FileForm(Integer fileId, String filename, String contentType, String filesize, Integer userId, byte[] filedata) {
        this.fileId = fileId;
        this.filename = filename;
        this.contentType = contentType;
        this.filesize = filesize;
        this.userId = userId;
        this.filedata = filedata;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public byte[] getFiledata() {
        return filedata;
    }

    public void setFiledata(byte[] filedata) {
        this.filedata = filedata;
    }
}
