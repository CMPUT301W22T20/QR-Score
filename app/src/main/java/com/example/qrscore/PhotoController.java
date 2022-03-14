package com.example.qrscore;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.qrscore.Photo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;
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

    public void downloadPhoto(String qrID, String userID, PhotoCallback photoCallback) {
        photoRef.whereEqualTo("qrID", qrID).whereEqualTo("uuid", userID).limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        try {
                            String photoPath = (String) queryDocumentSnapshots.getDocuments().get(0).getData().get("photoPath");
                            storageReference.child(photoPath).getDownloadUrl().addOnSuccessListener(photoCallback::onCallback);
                        } catch (IndexOutOfBoundsException e) {
                            return;
                        }
                    }
                });
    }
}
