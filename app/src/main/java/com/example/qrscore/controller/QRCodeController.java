package com.example.qrscore.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qrscore.model.Account;
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
    final String TAG = "QRCodeController";
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
     * @param accountController
     *      Instance of the AccountController
     * @param qrCode
     *      Instance of the QR Code.
     * @param uuid
     *      User UID of user that added it.
     */
    public void add(String hash, QRCode qrCode, String uuid, AccountController accountController) {
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
        accountColRef.document(uuid).update("qrCodes", FieldValue.arrayUnion(qrCode.getHash()));

        accountColRef.document(uuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot accountDocument = task.getResult();

                    String total = accountDocument.get("scanned").toString();
                    String score = accountDocument.get("score").toString();
                    String hiScore = accountDocument.get("hiscore").toString();
                    Log.i(TAG, "accDoc.get(\"score\").toString(): " + accountDocument.get("score").toString());

                    Account account = accountController.getAccount();

                    //Update total score
                    Integer updatedScore = account.getScore() + qrCode.getQRScore();
                    accountController.updateScore(updatedScore);

                    //Update high score if new code is higher
                    Integer hiscore = account.getHiscore();
                    if (qrCode.getQRScore() > hiscore) {
                        accountController.updateHiscore(qrCode.getQRScore());
                    }

                    //Update total scanned QR codes
                    Integer updatedTotalScanned = account.getScanned()+1;
                    accountController.updateTotalScanned(updatedTotalScanned);

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
                    Integer score = Integer.parseInt(accDoc.getString("score"))-qrCode.getQRScore();
                    Integer scanned = Integer.parseInt(accDoc.getString("scanned"))-1;
                    accountColRef.document(uuid).update("score", score.toString());
                    accountColRef.document(uuid).update("scanned", scanned.toString());
                }
            }
        });
    }
}
