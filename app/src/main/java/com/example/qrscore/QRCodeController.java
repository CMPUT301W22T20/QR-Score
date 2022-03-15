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

/**
 * Purpose: Represents a QRCodeController
 * - Add QR Codes to firebase and accounts.
 *
 * Outstanding issues:
 */
public class QRCodeController {
    private FirebaseFirestore db;
    private CollectionReference QRCodeColRef;
    private CollectionReference QRDataListRef;
    private DocumentReference QRDataListDocRef;
    private AccountController accountController;
    private QRDataList qrDataList;

    public QRCodeController() {
        db = FirebaseFirestore.getInstance();
        QRCodeColRef = db.collection("QRCode");
        QRDataListRef = db.collection("QRDataList");
    }

    /**
     * Purpose: To add a QR Code to firestore db.
     * @param key
     *      The hash of the QR Code.
     * @param qrCode
     *      Instance of the QR Code.
     * @param uuid
     *      User UID of user that added it.
     */
    public void add(String key, QRCode qrCode, String uuid) {
        QRDataListDocRef = QRDataListRef.document(uuid);

        QRDataListDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot qrDataDoc = task.getResult();
                if (qrDataDoc.exists()) {
                    qrDataList = qrDataDoc.toObject(QRDataList.class);
                }
            }
        });
        QRCodeColRef.document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot qrCodeDocument = task.getResult();
                    if (qrCodeDocument.exists()) {
                        ArrayList<String> scanners = (ArrayList<String>) qrCodeDocument.get("hasScanned");
                        if (!scanners.contains(uuid)) {
                            qrCodeDocument.getReference().update("hasScanned", FieldValue.arrayUnion(uuid));
                            addToQRDataList(key, qrDataList, qrCode);
                        }
                    }
                    else {
                        QRCodeColRef.document(key).set(qrCode);
                    }
                }
            }
        });
    }

    private void addToQRDataList(String key, QRDataList qrDataList, QRCode qrCode) {
//        qrDataList.setTotalQRCodesScanned(qrDataList.getTotalQRCodesScanned()+1);
//        qrDataList.setSumOfScoresScanned(qrDataList.getSumOfScoresScanned() + qrCode.getQRScore());
        QRDataListDocRef.update("qrCodes", FieldValue.arrayUnion(db.collection("QRCode").document(key)));
    }
}
