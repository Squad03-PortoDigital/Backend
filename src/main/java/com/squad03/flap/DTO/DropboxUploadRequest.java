package com.squad03.flap.DTO;

public class DropboxUploadRequest {
    private String path;
    private String fileName;
    private byte[] fileContent;

    public DropboxUploadRequest() {
    }

    public DropboxUploadRequest(String path, String fileName, byte[] fileContent) {
        this.path = path;
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}
