package com.example.qrscore;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.DocumentCollections;

import java.util.ArrayList;


public class QRCodeController {
    private FirebaseFirestore db;
    private CollectionReference QRCodeRef;
    private CollectionReference QRDataListRef;
    private DocumentReference QRDataListDocRef;
    private AccountController accountController;

    public QRCodeController() {
        db = FirebaseFirestore.getInstance();
        QRCodeRef = db.collection("QRCode");
        QRDataListRef = db.collection("QRDataList");
    }

    public void add(String key, QRCode qrCode, String uuid) {
        QRDataListDocRef = QRDataListRef.document(uuid);
        QRCodeRef.document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        ArrayList<String> scanners = (ArrayList<String>) doc.get("hasScanned");
                        if (!scanners.contains(uuid)) {
                            doc.getReference().update("hasScanned", FieldValue.arrayUnion(uuid));
                            addToQRDataList(key);
                        }
                    }
                    else {
                        QRCodeRef.document(key).set(qrCode);
                    }
                }
            }
        });
    }

    private void addToQRDataList(String key) {
        QRDataListDocRef.update("qrCodes", FieldValue.arrayUnion(key));
    }
}
