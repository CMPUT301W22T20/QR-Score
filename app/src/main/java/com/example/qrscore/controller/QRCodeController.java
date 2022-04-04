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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

/**
 * Purpose:
 * - Represents a QRCodeController
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
    private String newHiscore;

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

    private String getNewHiscore() {
        return newHiscore;
    }

    private void setNewHiscore(String thisUsersTopHiscore) {
        this.newHiscore = thisUsersTopHiscore;
    }

    /**
     * Purpose: Formatting strings to sort properly
     * @param value
     *     String to be formatted
     * @return appendedValue
     *     Formatted string
     */
    public String appendZeroes(String value) {
        Integer numZeroesToPrefix = 8 - value.length();
        String appendedValue = "";
        for (int i = 0; i < numZeroesToPrefix; i++) {
            appendedValue += "0";
        }
        appendedValue += value;
        return appendedValue;
    }

    /**
     * Purpose: To add a QR Code to firestore db.
     *
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
                        Log.i(TAG, "account.getTotalScore(): " + account.getTotalScore());
                        Log.i(TAG, "account.getTotalScanned(): " + account.getTotalScanned());
                        Log.i(TAG, "account.getHiscore(): " + account.getHiscore());

                        String updatedTotalScore;
                        String updatedTotalScanned;
                        String updatedHiscore;

                        Log.i(TAG, "accountDocument.get(\"totalScore\").toString(): " + accountDocument.get("totalScore").toString());

                        //Update total score
                        Integer updatedTotalScoreInt = Integer.parseInt(accountDocument.get("totalScore").toString()) + Integer.parseInt(qrCode.getQRScore());
                        updatedTotalScore = appendZeroes(updatedTotalScoreInt.toString());
                        account.setTotalScore(updatedTotalScore);

                        //Update total scanned QR codes
                        Integer updatedTotalScannedInt = Integer.parseInt(accountDocument.get("totalScanned").toString())+1;
                        updatedTotalScanned = appendZeroes(updatedTotalScannedInt.toString());
                        account.setTotalScanned(updatedTotalScanned);

                        //Update high score if new code is higher
                        Integer currentHiscore = Integer.parseInt(accountDocument.get("hiscore").toString());
                        Integer newQRCodeScore = Integer.parseInt(qrCode.getQRScore());
                        if (newQRCodeScore > currentHiscore) {
                            updatedHiscore = appendZeroes(newQRCodeScore.toString());
                        }
                        else {
                            updatedHiscore = appendZeroes(currentHiscore.toString());
                        }
                        account.setHiscore(updatedHiscore);

                        //Upload updated Account data to firebase
                        accountController.updateAccount(updatedTotalScore, updatedTotalScanned, updatedHiscore);

                        //Upload updated Rank data to firebase
                        accountController.refreshRanks();

                        accountController.setAccount(account);
                    }
                }
            }
        });
    }

    /**
     * Purpose: To remove a QR Code to firestore db.
     * @param hash
     *      The hash of the QR Code.
     * @param accountController
     *      Instance of the AccountController
     * @param qrCode
     *      Instance of the QR Code.
     * @param uuid
     *      User UID of user that removed it.
     */
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
                    String updatedTotalScore;
                    String updatedTotalScanned;
                    String updatedHiscore;

                    //Update total score
                    Integer updatedTotalScoreInt = Integer.parseInt(accountDocument.get("totalScore").toString()) - Integer.parseInt(qrCode.getQRScore());
                    updatedTotalScore = appendZeroes(updatedTotalScoreInt.toString());

                    //Update total scanned QR codes
                    Integer updatedTotalScannedInt = Integer.parseInt(accountDocument.get("totalScanned").toString()) - 1;
                    updatedTotalScanned = appendZeroes(updatedTotalScannedInt.toString());

                    //Update high score if deleted code is current high score
                    //Update high score if new code is higher
                    Integer currentHiscore = Integer.parseInt(accountDocument.get("hiscore").toString());
                    Integer deletedQRCodeScore = Integer.parseInt(qrCode.getQRScore());
                    if (deletedQRCodeScore == currentHiscore) {
                        getNextHighestScore(qrCodeRef, accountController, updatedTotalScore, updatedTotalScanned);
                    }
                    else {
                        updatedHiscore = appendZeroes(currentHiscore.toString());
                        Log.i(TAG, "updatedHiscore: " + updatedHiscore);
                        //Upload updated Account data to firebase
                        accountController.updateAccount(updatedTotalScore, updatedTotalScanned, updatedHiscore);
                        //Upload updated Rank data to firebase
                        accountController.refreshRanks();
                    }
                }
            }
        });
    }

    /**
     * Purpose: Calculate a player's next highest score when they delete their highest scoring QR code
     * @param qrCodeRef
     *        a reference to the QR Code data in firebase
     * @param accountController
     *        an instance of the account controller
     * @param updatedTotalScore
     *        the updated total score of the player removing the QR code
     * @param updatedTotalScanned
     *        the updated total scanned codes of the player removing the QR code
     */
    public void getNextHighestScore(DocumentReference qrCodeRef, AccountController accountController, String updatedTotalScore, String updatedTotalScanned) {
        Account account = accountController.getAccount();
        Log.i(TAG, "account.getUserUID(): " + account.getUserUID());
        Query qrSortByScore = qrCodeRef.getParent().orderBy("qrscore", Query.Direction.DESCENDING);
//                        .whereEqualTo("userUID", account.getUserUID())
        qrSortByScore.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> qrCodeDocuments = task.getResult().getDocuments();
                    DocumentReference accountDocumentRef;
                    Integer thisUsersHiscoreRank = 1;
                    for (DocumentSnapshot qrCodeDocument: qrCodeDocuments) {
                        if (thisUsersHiscoreRank == 2) {
                            break;
                        }
                        //Update user's hiscore rank

                        String thisQRScore = qrCodeDocument.getData().get("qrscore").toString();
//                                        String thisUsersUID = qrCodeDocument.getData().get("hasScanned").toString();
                        ArrayList<String> hasScanned = (ArrayList<String>) qrCodeDocument.get("hasScanned");
                        Log.i(TAG, "thisQRScore: " + thisQRScore);
                        for (String thisUsersUID: hasScanned) {
                            Log.i(TAG, "thisUsersUID: " + thisUsersUID);
                            Log.i(TAG, "account.getUserUID(): " + account.getUserUID());
                            if (thisUsersUID.equals(account.getUserUID())) {
                                Log.i(TAG, thisUsersUID + "'s new Top Hiscore: " + thisQRScore);
                                String updatedHiscore = appendZeroes(thisQRScore);
                                account.setHiscore(updatedHiscore);
                                Log.i(TAG, "account.getHiscore(): " + account.getHiscore());
                                accountController.updateAccount(updatedTotalScore, updatedTotalScanned, updatedHiscore);
                                accountController.refreshRanks();

                                thisUsersHiscoreRank += 1;
                                break;
                            }
                        }
                    }
                }
            }

        });

    }
}
