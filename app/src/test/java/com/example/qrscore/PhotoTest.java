package com.example.qrscore;

import static org.junit.Assert.assertEquals;

import com.example.qrscore.model.Photo;

import org.junit.Test;

import java.util.ArrayList;

public class PhotoTest {
    ArrayList<Photo> photosList = new ArrayList<>();

    @Test
    public void testCreatePhoto() {
        String uuid = "testUUID";
        String photoPath = "path/testPath";
        String qrID = "testQrID";

        assertEquals(0, photosList.size());

        Photo photo = new Photo(photoPath, qrID, uuid);

        photosList.add(photo);

        assertEquals(1, photosList.size());
    }

    @Test
    public void testGetters() {
        String uuid = "testUUID";
        String photoPath = "path/testPath";
        String qrID = "testQrID";

        Photo photo = new Photo(photoPath, qrID, uuid);

        assertEquals(uuid, photo.getUUID());

        assertEquals(photoPath, photo.getPhotoPath());

        assertEquals(qrID, photo.getQrID());
    }

    @Test
    public void testSetters() {
        String uuid = "testUUID";
        String photoPath = "path/testPath";
        String qrID = "testQrID";

        Photo photo = new Photo(photoPath, qrID, uuid);

        assertEquals(photoPath, photo.getPhotoPath());

        String newPhotoPath = "newPath/newTestPath";

        photo.setPhotoPath(newPhotoPath);

        assertEquals(newPhotoPath, photo.getPhotoPath());
    }
}
