package com.example.qrscore.controller;

import androidx.annotation.NonNull;

import com.example.qrscore.model.QRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private CollectionReference accountColRef;

    public QRCodeController() {
        db = FirebaseFirestore.getInstance();
        QRCodeColRef = db.collection("QRCode");
        accountColRef = db.collection("Account");
    }

    /**
     * Purpose: To add a QR Code to firestore db.
     * @param hash
     *      The hash of the QR Code.
     * @param qrCode
     *      Instance of the QR Code.
     * @param uuid
     *      User UID of user that added it.
     */
    public void add(String hash, QRCode qrCode, String uuid) {
        QRCodeColRef.document(hash).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot qrCodeDocument = task.getResult();
                    if (qrCodeDocument.exists()) {
                        ArrayList<String> scanners = (ArrayList<String>) qrCodeDocument.get("scanned");
                        if (!scanners.contains(uuid)) {
                            qrCodeDocument.getReference().update("scanned", FieldValue.arrayUnion(uuid));
                        }
                    }
                    else {
                        QRCodeColRef.document(hash).set(qrCode);
                    }
                }
            }
        });
        accountColRef.document(uuid).update("QRCodes", FieldValue.arrayUnion(qrCode.getHash()));

        accountColRef.document(uuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot accDoc = task.getResult();
                    Integer score = Integer.parseInt(accDoc.getString("Score"))+qrCode.getQRScore();
                    Integer total = Integer.parseInt(accDoc.getString("Total"))+1;
                    accountColRef.document(uuid).update("Score", score.toString());
                    accountColRef.document(uuid).update("Total", total.toString());
                }
            }
        });
    }

    public void remove(String hash, QRCode qrCode, String uuid) {
        if (qrCode == null) {
            throw new IllegalArgumentException("No valid QRCode was passed into this function.");
        }

        // Get document references
        DocumentReference qrCodeRef = db.collection("QRCode").document(hash);
        DocumentReference accountRef = db.collection("Account").document(uuid);

        // Remove code from hasScanned and Account
        qrCodeRef.update("scanned", FieldValue.arrayRemove(uuid));
        accountRef.update("QRCodes", FieldValue.arrayRemove(hash));

        accountColRef.document(uuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot accDoc = task.getResult();
                    Integer score = Integer.parseInt(accDoc.getString("Score"))-qrCode.getQRScore();
                    Integer total = Integer.parseInt(accDoc.getString("Total"))-1;
                    accountColRef.document(uuid).update("Score", score.toString());
                    accountColRef.document(uuid).update("Total", total.toString());
                }
            }
        });
    }
}
