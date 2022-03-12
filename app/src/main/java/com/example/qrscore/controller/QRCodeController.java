package com.example.qrscore.controller;

import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class QRCodeController {
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    public QRCodeController() {
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("QRCode");
    }
}
