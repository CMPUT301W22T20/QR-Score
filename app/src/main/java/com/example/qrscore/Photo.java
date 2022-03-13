package com.example.qrscore;

import android.net.Uri;

public class Photo {
    private String qrID;
    private String UUID;
    private String photoPath;

    public Photo(String photoPath, String qrID, String UUID) {
        this.qrID = qrID;
        this.UUID = UUID;
        this.photoPath = photoPath;
    }

    public String getQrID() {
        return qrID;
    }

    public String getUUID() {
        return UUID;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String path) {
        photoPath = path;
    }

}
