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
     * @param newAccount
     *      Account to be set/updated with locally.
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

    /**
     * Purpose: Formatting strings to sort properly
     * @param value
     *     String to be formatted
     * @return appendedValue
     *     Formatted string
     */
    public String appendZeroes(String value) {
        Integer numZeroesToPrefix = 4 - value.length();
        String appendedValue = "";
        for (int i = 0; i < numZeroesToPrefix; i++) {
            appendedValue += "0";
        }
        appendedValue += value;
        return appendedValue;
    }

    /**
     * Purpose: Update everyone's ranks based on new stats
     */
    public void refreshRanks() {
        Log.i(TAG, "Refreshing Account ranks");

        Query accountSortByTotalScore = accountCollectionRef.orderBy("totalScore", Query.Direction.DESCENDING);
        accountSortByTotalScore.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> accountDocuments = task.getResult().getDocuments();
                    Integer newTotalScoreRank = 1;
                    for (DocumentSnapshot accountDocument : accountDocuments) {
                        //Update user's total score rank
                        accountDocumentRef = accountCollectionRef.document(accountDocument.getData().get("userUID").toString());

                        String updatedTotalScoreRank = appendZeroes(newTotalScoreRank.toString());
                        accountDocumentRef.update("rankTotalScore", updatedTotalScoreRank);
                        newTotalScoreRank++;

                        String topTotalScore = (String) accountDocument.getData().get("totalScore");
                        String topUID = (String) accountDocument.getData().get("userUID");
                        Log.i(TAG, "Total Score Rank " + updatedTotalScoreRank + ": " + topUID + "(" + topTotalScore + ")");
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
                    for (DocumentSnapshot accountDocument : accountDocuments) {
                        //Update user's total scanned rank
                        accountDocumentRef = accountCollectionRef.document(accountDocument.getData().get("userUID").toString());

                        String updatedTotalScannedRank = appendZeroes(newTotalScannedRank.toString());
                        accountDocumentRef.update("rankTotalScanned", updatedTotalScannedRank);
                        newTotalScannedRank++;

                        String topTotalScanned = (String) accountDocument.getData().get("totalScanned");
                        String topUID = (String) accountDocument.getData().get("userUID");
                        Log.i(TAG, "Total Scanned Rank " + updatedTotalScannedRank + ": " + topUID + "(" + topTotalScanned + ")");
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
                    for (DocumentSnapshot accountDocument : accountDocuments) {
                        //Update user's hiscore rank
                        accountDocumentRef = accountCollectionRef.document(accountDocument.getData().get("userUID").toString());

                        String updatedHiscoreRank = appendZeroes(newHiscoreRank.toString());
                        accountDocumentRef.update("rankHiscore", updatedHiscoreRank);
                        newHiscoreRank++;

                        String topHiscore = (String) accountDocument.getData().get("hiscore");
                        String topUID = (String) accountDocument.getData().get("userUID");
                        Log.i(TAG, "Hiscore Rank " + updatedHiscoreRank + ": " + topUID + "(" + topHiscore + ")");
                    }
                }
            }
        });
    }

    /**
     * Purpose: Updates the current player's stats on firestore db and locally.
     *
     * @param updatedTotalScore An instance of their updated high score.
     * @param updatedTotalScanned An instance of their updated high score.
     * @param updatedHiscore An instance of their updated high score.
     */
    public void updateAccount(String updatedTotalScore, String updatedTotalScanned, String updatedHiscore) {
        accountDocumentRef = accountCollectionRef.document(currentUser.getUid());
        accountDocumentRef.update("totalScore", updatedTotalScore);
        accountDocumentRef.update("totalScanned", updatedTotalScanned);
        accountDocumentRef.update("hiscore", updatedHiscore);
    }

    /**
     * Purpose: Add an accountListener for firestore data.
     */
    public void addAccountListener() {
        userUID = currentUser.getUid();
        profileRef = db.collection("Profile").document(userUID);
        accountListener = profileRef.addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                Account savedAccount = snapshot.toObject(Account.class);
                setAccount(savedAccount);    // Update profile locally
                Log.d(TAG, savedAccount.getUserUID() + " account snapshot exists!");
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
    }

    /**
     * Purpose: Remove accountListener when player leaves HomeFragment.
     */
    public void removeAccountListener() {
        accountListener.remove();
    }
}