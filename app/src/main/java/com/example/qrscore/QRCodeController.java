package com.example.qrscore;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class QRCodeController {
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    public QRCodeController() {
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("QRCode");
    }

    public void add(String key, QRCode qrCode, String uuid) {
        collectionReference.document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        ArrayList<String> scanners = (ArrayList<String>) doc.get("hasScanned");
                        if (!scanners.contains(uuid)) {
                            doc.getReference().update("hasScanned", FieldValue.arrayUnion(uuid));
                        }
                    }
                    else {
                        collectionReference.document(key).set(qrCode);
                    }
                }
            }
        });
    }
}
