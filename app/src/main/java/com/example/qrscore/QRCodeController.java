package com.example.qrscore;

import com.example.qrscore.QRCode;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class QRCodeController {
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    public QRCodeController() {
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("QRCode");
    }

    public boolean qrExists(String hash) {
        return collectionReference.document(hash).get().isSuccessful();
    }

    public void add(String key, QRCode qrCode) {
        collectionReference.document(key).set(qrCode);
    }
//    public void updateHasScanned(String codeID, Player player)
}
