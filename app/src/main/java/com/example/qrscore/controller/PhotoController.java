package com.example.qrscore.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.qrscore.model.Photo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Purpose: A controller to interact with the photos and firebase storage.
 *
 * Outstanding Issues:
 */
public class PhotoController {
    private FirebaseFirestore db;
    private CollectionReference photoRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Context context;

    public PhotoController(Context context) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        this.context = context;

        db = FirebaseFirestore.getInstance();
        photoRef = db.collection("Photo");
    }

    /**
     * Purpose: Compresses and uploads a Photo to storage.
     * How to compress: https://stackoverflow.com/questions/41611294/how-to-reduce-the-size-of-image-before-uploading-it-to-firebase-storage
     *
     * @param photo
     *      A photo instance.
     * @param imageUri
     *      ImageUri instance.
     */
    public void uploadPhoto(Photo photo, Uri imageUri) {
        StorageReference imageRef = storageReference.child(photo.getPhotoPath());

        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 8, baos);
        byte[] data = baos.toByteArray();

        //uploading the image
        UploadTask uploadTask2 = imageRef.putBytes(data);

        uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("PhotoController", "Photo uploaded successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("PhotoController", "Photo not uploaded successfully");
            }
        });

        photoRef.add(photo);

    }

    /**
     * Purpose: Download a photo from storage.
     *
     * @param qrID
     *      The QR hash ID.
     * @param userID
     *      A users unique UID.
     * @param photoCallback
     *      A photoCallback Instance.
     */
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
