package com.example.qrscore.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qrscore.model.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

/**
 * Purpose: Controller for accounts.
 *
 * Outstanding issues:
 *
 */
public class AccountController {
    private final String TAG = "ACCOUNT_CONTROLLER";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private CollectionReference accountCollectionRef;
    private CollectionReference QRDataListCollectionRef;
    private CollectionReference profileCollectionRef;
    private DocumentReference accountDocumentRef;
    private DocumentReference QRDataListRef;
    private DocumentReference profileRef;
    private DocumentSnapshot accountSnapshot;
    private DocumentSnapshot QRDataListSnapshot;
    private DocumentSnapshot profileSnapshot;
    private String userUID;
    private Account newAccount;
    private Account savedAccount;
    private SharedPreferences accountSP;
    private SharedPreferences.Editor accountSPEditor;
    private ListenerRegistration accountListener;

    private static final String ACCOUNT_PREFS = "accountPrefs";

    public AccountController(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        accountSP = context.getSharedPreferences(ACCOUNT_PREFS, Context.MODE_PRIVATE);
        accountSPEditor = accountSP.edit();
        accountCollectionRef = db.collection("Account");
        profileCollectionRef = db.collection("Profile");
    }

    /**
     * Purpose: Create an account on firestore.
     */
    public void createNewAccount(String userUID) {
        Log.i(TAG, "Creating new account for uid " + userUID);
        newAccount = new Account(userUID);
        accountDocumentRef = accountCollectionRef.document(userUID);
        HashMap<String, Object> account = new HashMap<>();
        account.put("userUID", newAccount.getUserUID());
        account.put("qrCodes", newAccount.getQRCodesList());
        account.put("totalScore", newAccount.getTotalScore());
        account.put("totalScanned", newAccount.getTotalScanned());
        account.put("hiscore", newAccount.getHiscore());
        account.put("rankTotalScore", newAccount.getRankTotalScore());
        account.put("rankTotalScanned", newAccount.getRankTotalScanned());
        account.put("rankHiscore", newAccount.getRankHiscore());
        account.put("isOwner", "false");
        accountDocumentRef.set(account)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Account created!");
                    setAccount(newAccount);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Account has not been created.");

                    //Should this be here?
                    accountDocumentRef.set(newAccount);
                });
    }

    /**
     * Purpose: Set/Update account info in SharedPrefs.
     *
     * @param newAccount Account to be set/updated with locally.
     */
    public void setAccount(Account newAccount) {
        accountSPEditor.putString("userUID", newAccount.getUserUID());
        accountSPEditor.putString("totalScore", (newAccount.getTotalScore()));
        accountSPEditor.putString("totalScanned", (newAccount.getTotalScanned()));
        accountSPEditor.putString("hiscore", (newAccount.getHiscore()));
        accountSPEditor.putString("rankTotalScore", (newAccount.getRankTotalScore()));
        accountSPEditor.putString("rankTotalScanned", (newAccount.getRankTotalScanned()));
        accountSPEditor.putString("rankHiscore", (newAccount.getRankHiscore()));
        accountSPEditor.apply();
    }

    /**
     * Purpose: Return an instance of the account saved locally
     *
     * @return Represents the Account object locally.
     */
    public Account getAccount() {
        String userUID = accountSP.getString("userUID", currentUser.getUid());
        String totalScore = accountSP.getString("totalScore", null);
        String totalScanned = accountSP.getString("totalScanned", null);
        String hiscore = accountSP.getString("hiscore", null);
        String totalScoreRank = accountSP.getString("rankTotalScore", null);
        String totalScannedRank = accountSP.getString("rankTotalScanned", null);
        String hiscoreRank = accountSP.getString("rankHiscore", null);
        Account account = new Account(userUID, totalScore, totalScanned, hiscore,
                totalScoreRank, totalScannedRank, hiscoreRank);
        return account;
    }

    public void refreshRanks() {
        Log.i(TAG, "Refreshing Account ranks");

        Query accountSortByTotalScore = accountCollectionRef.orderBy("totalScore", Query.Direction.DESCENDING);
        accountSortByTotalScore.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> accountDocuments = task.getResult().getDocuments();
                    Integer newTotalScoreRank = 1;
                    for (DocumentSnapshot accountDocument: accountDocuments) {
                        //Update user's total score rank
                        accountDocumentRef = accountCollectionRef.document(accountDocument.getData().get("userUID").toString());

                        Integer numZeroesToPrefix = 8 - newTotalScoreRank.toString().length();
                        String updatedTotalScoreRank = "";
                        for (int i = 0; i < numZeroesToPrefix; i++) {
                            updatedTotalScoreRank += "0";
                        }
                        updatedTotalScoreRank += newTotalScoreRank.toString();

                        accountDocumentRef.update("rankTotalScore", updatedTotalScoreRank.toString());

                        String topTotalScore = (String) accountDocument.getData().get("totalScore");
                        String topUID = (String) accountDocument.getData().get("userUID");
                        Log.i(TAG, "Total Score Rank " + updatedTotalScoreRank + ": " + topUID + "(" + topTotalScore + ")");
                        //TODO:  Checking if the accountDocument contains the unique qrID
                        newTotalScoreRank++;
                    }
                }
            }
        });

        Query accountSortByTotalScanned = accountCollectionRef.orderBy("totalScanned", Query.Direction.DESCENDING);
        accountSortByTotalScanned.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> accountDocuments = task.getResult().getDocuments();
                    Integer newTotalScannedRank = 1;
                    for (DocumentSnapshot accountDocument: accountDocuments) {
                        //Update user's total scanned rank
                        accountDocumentRef = accountCollectionRef.document(accountDocument.getData().get("userUID").toString());

                        Integer numZeroesToPrefix = 8 - newTotalScannedRank.toString().length();
                        String updatedTotalScannedRank = "";
                        for (int i = 0; i < numZeroesToPrefix; i++) {
                            updatedTotalScannedRank += "0";
                        }
                        updatedTotalScannedRank += newTotalScannedRank.toString();

                        accountDocumentRef.update("rankTotalScanned", updatedTotalScannedRank.toString());

                        String topTotalScanned = (String) accountDocument.getData().get("totalScanned");
                        String topUID = (String) accountDocument.getData().get("userUID");
                        Log.i(TAG, "Total Scanned Rank " + updatedTotalScannedRank + ": " + topUID + "(" + topTotalScanned + ")");

                        //TODO: Checking if the accountDocument contains the unique qrID
                        newTotalScannedRank++;
                    }
                }
            }
        });

        Query accountSortByHiscore = accountCollectionRef.orderBy("hiscore", Query.Direction.DESCENDING);
        accountSortByHiscore.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> accountDocuments = task.getResult().getDocuments();
                    Integer newHiscoreRank = 1;
                    for (DocumentSnapshot accountDocument: accountDocuments) {
                        //Update user's hiscore rank
                        accountDocumentRef = accountCollectionRef.document(accountDocument.getData().get("userUID").toString());

                        Integer numZeroesToPrefix = 8 - newHiscoreRank.toString().length();
                        String updatedHiscoreRank = "";
                        for (int i = 0; i < numZeroesToPrefix; i++) {
                            updatedHiscoreRank += "0";
                        }
                        updatedHiscoreRank += newHiscoreRank.toString();

                        accountDocumentRef.update("rankHiscore", updatedHiscoreRank.toString());

                        String top5hiscore = (String) accountDocument.getData().get("hiscore");
                        String top5uid = (String) accountDocument.getData().get("userUID");
                        Log.i(TAG, "Hiscore Rank " + updatedHiscoreRank + ": " + top5uid + "(" + top5hiscore + ")");
                        //TODO:  Checking if the accountDocument contains the unique qrID
                        newHiscoreRank++;
                    }
                }
            }
        });
    }

    /**
     * Purpose: Updates the current player's total scanned QR codes on firestore db and locally.
     *
     * @param updatedTotal An instance of their updated total scanned QR codes.
     */
    public void updateTotalScanned(String updatedTotal) {
        accountDocumentRef = accountCollectionRef.document(currentUser.getUid());
    }

    /**
     * Purpose: Updates the current player's high score on firestore db and locally.
     *
     * @param updatedHiscore An instance of their updated high score.
     */
    public void updateHiscore(String updatedHiscore) {
        accountDocumentRef = accountCollectionRef.document(currentUser.getUid());
    }

}
//    public void getNewAccount() {
//        collectionReference = db.collection("Account");
//
//        // Checks if the userID exists in the db.
//        // From: https://firebase.google.com/docs/firestore/query-data/get-data#get_a_document
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot doc = task.getResult();
//                    if (doc.exists()) {
//                        // If it does, retrieve data from the account doc in
//                        // the db to build the account object.
//                        Map<String, Object> data = doc.getData();
//                        totalScore[0] = Integer.parseInt(data.get("totalScore"));
//                        scanned[0] = Integer.parseInt(data.get("totalScanned"));
//                        System.out.println("In docref: "+totalScore[0]+" "+scanned[0]);
//                    } else {
//                        System.out.println("Doc does not exist, creating new.");
//                        // Else, initialize everything with default(?) values
//                        // and then create a new account doc in the db.
//                        HashMap<String, Object> data = new HashMap<>();
//
//                        // From stackoverflow
//                        // https://stackoverflow.com/questions/51292378/how-do-you-insert-a-reference-value-into-firestore
//                        // https://stackoverflow.com/users/3095195/trojek
//                        data.put("Profile", db.collection("Profile").document(userID));
//
//                        data.put("totalScore", "0");
//                        data.put("totalScanned", "0");
//                        collectionReference.document(userID)
//                                .set(data)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Log.d(TAG, "Data added successfully.");
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.d(TAG, "Data could not be added. " + e);
//                                    }
//                                });
//                    }
//                } else {
//                    Log.d(TAG,"Get failed with ", task.getException());
//                }
//            }
//        });
//        System.out.println("Outside docref: "+totalScore[0]+" "+scanned[0]);
//    }

//    public Account createNewAccount() {
//        System.out.println("In create: "+totalScore[0]+" "+scanned[0]);
//        return new Account(userID);
//    }
//}