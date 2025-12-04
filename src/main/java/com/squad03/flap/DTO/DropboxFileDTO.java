package com.squad03.flap.DTO;

import java.time.LocalDateTime;

public class DropboxFileDTO {
    private String id;
    private String name;
    private String path;
    private String type; // "folder" ou "file"
    private Long size;
    private LocalDateTime modified;
    private String thumbnailUrl;
    private String downloadUrl;
    private String previewUrl;

    public DropboxFileDTO() {
    }

    public DropboxFileDTO(String id, String name, String path, String type, Long size, LocalDateTime modified) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
        this.size = size;
        this.modified = modified;
    }

    // ✅ Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }
}
