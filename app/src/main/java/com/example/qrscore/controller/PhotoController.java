package com.example.qrscore.controller;

import android.net.Uri;

import com.example.qrscore.Photo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class PhotoController {
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public PhotoController() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void uploadPhoto(Photo photo) {
        final String imageKey = UUID.randomUUID().toString();
        Uri imageUri = photo.getImageUri();
        StorageReference imageRef = storageReference.child("images/" + imageKey);

        imageRef.putFile(imageUri);
    }

    public void downloadPhoto() {
        StorageReference pathRef = storageReference.getRoot();
        System.out.println(pathRef);
    }
}
