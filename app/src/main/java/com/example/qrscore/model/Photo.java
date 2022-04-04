package com.example.qrscore.model;

/**
 * Purpose: Represents a Photo instance.
 *
 * Outstanding issues:
 */
public class Photo {
    private String qrID;
    private String UUID;
    private String photoPath;

    public Photo(String photoPath, String qrID, String UUID) {
        this.qrID = qrID;
        this.UUID = UUID;
        this.photoPath = photoPath;
    }

    /**
     * Purpose: Get the QR Code ID for photo.
     *
     * @return
     *      qrID of photo
     */
    public String getQrID() {
        return qrID;
    }

    /**
     * Purpose: Get the user who took the photo.
     *
     * @return
     *      userUID of user that took the photo.
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * Purpose: Get the photo path to the photo.
     *
     * @return
     *      String representing the photo path.
     */
    public String getPhotoPath() {
        return photoPath;
    }

    /**
     * Purpose: Set the photo path for the photo
     * @param path
     *      String representing the photo path.
     */
    public void setPhotoPath(String path) {
        photoPath = path;
    }

}
