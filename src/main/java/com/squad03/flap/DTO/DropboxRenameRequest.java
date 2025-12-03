package com.squad03.flap.DTO;

public class DropboxRenameRequest {
    private String fromPath;
    private String toPath;

    public DropboxRenameRequest() {
    }

    public DropboxRenameRequest(String fromPath, String toPath) {
        this.fromPath = fromPath;
        this.toPath = toPath;
    }

    public String getFromPath() {
        return fromPath;
    }

    public void setFromPath(String fromPath) {
        this.fromPath = fromPath;
    }

    public String getToPath() {
        return toPath;
    }

    public void setToPath(String toPath) {
        this.toPath = toPath;
    }
}
