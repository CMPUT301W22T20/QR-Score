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
    private CollectionReference QRCodeColRef;
    private CollectionReference QRDataListRef;
    private DocumentReference QRDataListDocRef;
    private AccountController accountController;

    public QRCodeController() {
        db = FirebaseFirestore.getInstance();
        QRCodeColRef = db.collection("QRCode");
        QRDataListRef = db.collection("QRDataList");
    }

    public void add(String key, QRCode qrCode, String uuid) {
        int currentScore;
        int currentNumCodesScanned;
        QRDataListDocRef = QRDataListRef.document(uuid);
        QRDataListRef.document(uuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot qrDataListDocument = task.getResult();
                    if (qrDataListDocument.exists()) {
                        currentScore = ((Number) qrDataListDocument.get("sumOfScoresScanned")).intValue();
                        currentNumCodesScanned = ((Number) qrDataListDocument.get("totalQRCodesScanned")).intValue();
                        ArrayList<String> scanners = (ArrayList<String>) qrDataListDocument.get("hasScanned");
                        if (!scanners.contains(uuid)) {
                            qrDataListDocument.getReference().update("hasScanned", FieldValue.arrayUnion(uuid));
                            addToQRDataList(key);
                        }
                    }
                    else {
                        QRCodeColRef.document(key).set(qrCode);
                    }
                }
            }
        });
        QRCodeColRef.document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot qrCodeDocument = task.getResult();
                    if (qrCodeDocument.exists()) {
                        int addToScore = ((Number) qrCodeDocument.get("qrscore")).intValue();
                        ArrayList<String> scanners = (ArrayList<String>) qrCodeDocument.get("hasScanned");
                        if (!scanners.contains(uuid)) {
                            qrCodeDocument.getReference().update("hasScanned", FieldValue.arrayUnion(uuid));
                            addToQRDataList(key);
                        }
                    }
                    else {
                        QRCodeColRef.document(key).set(qrCode);
                    }
                }
            }
        });
    }

    private void addToQRDataList(String key) {
        QRDataListDocRef.update("qrCodes", FieldValue.arrayUnion(db.collection("QRCode").document(key)));
    }
}
