package com.example.qrscore;

import android.net.Uri;

import com.example.qrscore.Photo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class PhotoController {
    private FirebaseFirestore db;
    private CollectionReference photoRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public PhotoController() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        db = FirebaseFirestore.getInstance();
        photoRef = db.collection("Photo");
    }

    public void uploadPhoto(Photo photo, Uri imageUri) {
        StorageReference imageRef = storageReference.child(photo.getPhotoPath());

        imageRef.putFile(imageUri);
        photoRef.add(photo);
    }

    public void downloadPhoto() {
        StorageReference pathRef = storageReference.getRoot();
        System.out.println(pathRef);
    }
}
