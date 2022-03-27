package com.example.qrscore.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qrscore.model.Account;
import com.example.qrscore.model.QRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
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
     * @param key
     *      The hash of the QR Code.
     * @param qrCode
     *      Instance of the QR Code.
     * @param uuid
     *      User UID of user that added it.
     */
    public void add(String key, QRCode qrCode, String uuid, AccountController accountController) {
        QRCodeColRef.document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                        QRCodeColRef.document(key).set(qrCode);
                    }
                }
            }
        });
        accountColRef.document(uuid).update("QRCodes", FieldValue.arrayUnion(qrCode.getHash()));

        // TODO: Add functionality to update Score & Total in FireBase Account collection
        accountColRef.document(uuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot accDoc = task.getResult();

                    Log.i(TAG, "accDoc.getString(\"Score\"): " + accDoc.getString("Score"));

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
}
