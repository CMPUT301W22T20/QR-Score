package com.example.qrscore.controller;

import android.util.Log;
import android.widget.Toast;

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
    private boolean playerHasAlreadyScannedThisCode;

    public QRCodeController() {
        db = FirebaseFirestore.getInstance();
        QRCodeColRef = db.collection("QRCode");
        accountColRef = db.collection("Account");
    }

    public boolean getPlayerHasAlreadyScannedThisCode() {
        return playerHasAlreadyScannedThisCode;
    }

    public void setPlayerHasAlreadyScannedThisCode(boolean playerHasAlreadyScannedThisCode) {
        this.playerHasAlreadyScannedThisCode = playerHasAlreadyScannedThisCode;
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
        //Add account UUID to QRCode's hasScanned list
        QRCodeColRef.document(hash).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot qrCodeDocument = task.getResult();
                    if (qrCodeDocument.exists()) {
                        ArrayList<String> hasScanned = (ArrayList<String>) qrCodeDocument.get("hasScanned");
                        //Check if player has already scanned this QRCode
                        if (!hasScanned.contains(uuid)) {
                            setPlayerHasAlreadyScannedThisCode(false);
                            qrCodeDocument.getReference().update("hasScanned", FieldValue.arrayUnion(uuid));
                        }
                        else {
                            setPlayerHasAlreadyScannedThisCode(true);
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

                    String total = accountDocument.get("totalScanned").toString();
                    String score = accountDocument.get("totalScore").toString();
                    String hiScore = accountDocument.get("hiscore").toString();
                    Log.i(TAG, "accDoc.get(\"score\").toString(): " + accountDocument.get("totalScore").toString());

                    //Don't update stats if player has already scanned this QR Code
                    if (getPlayerHasAlreadyScannedThisCode() == true) {
                        Log.i(TAG, "Player " + uuid + " has already scanned QR Code " + hash);
                    }
                    else {
                        Account account = accountController.getAccount();

                        //Update total score
                        Integer updatedScore = Integer.parseInt(account.getTotalScore()) + Integer.parseInt(qrCode.getQRScore());
                        accountController.updateScore(updatedScore.toString());

                        //Update high score if new code is higher
                        Integer currentHiscore = Integer.parseInt(account.getHiscore());
                        Integer newQRCodeScore = Integer.parseInt(qrCode.getQRScore());
                        if (newQRCodeScore > currentHiscore) {
                            accountController.updateHiscore(newQRCodeScore.toString());
                        }

                        //Update total scanned QR codes
                        Integer updatedTotalScanned = Integer.parseInt(account.getTotalScanned())+1;
                        accountController.updateTotalScanned(updatedTotalScanned.toString());
                    }
                }
            }
        });
    }

    public void remove(String hash, QRCode qrCode, String uuid, AccountController accountController) {
        if (qrCode == null) {
            throw new IllegalArgumentException("No valid QRCode was passed into this function.");
        }

        // Get document references
        DocumentReference qrCodeRef = db.collection("QRCode").document(hash);
        DocumentReference accountRef = db.collection("Account").document(uuid);

        // Remove code from hasScanned and Account
        qrCodeRef.update("hasScanned", FieldValue.arrayRemove(uuid));
        accountRef.update("qrCodes", FieldValue.arrayRemove(hash));

        accountColRef.document(uuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot accountDocument = task.getResult();

                    Account account = accountController.getAccount();

                    //Subtract from total score
                    Integer updatedScore = Integer.parseInt(account.getTotalScore()) - Integer.parseInt(qrCode.getQRScore());
                    accountController.updateScore(updatedScore.toString());

                    //Update high score if deleted code is current high score
                    Integer currentHiscore = Integer.parseInt(account.getHiscore());
                    Integer deletedQRCodeScore = Integer.parseInt(qrCode.getQRScore());
                    if (deletedQRCodeScore == currentHiscore) {
                        //TODO: Recalculate next-highest hiscore
                        accountController.updateHiscore("0");
                    }

                    //Update total scanned QR codes
                    Integer updatedTotalScanned = Integer.parseInt(account.getTotalScanned())-1;
                    accountController.updateTotalScanned(updatedTotalScanned.toString());
                }
            }
        });
    }
}
