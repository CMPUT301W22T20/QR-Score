package com.example.qrscore.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.qrscore.model.Account;
import com.example.qrscore.model.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;

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
    private DocumentReference accountRef;
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
        accountRef = accountCollectionRef.document(userUID);

        HashMap<String, Object> account = new HashMap<>();
        account.put("userUID", newAccount.getUserUID());
        account.put("score", newAccount.getScore());
        account.put("hiscore", newAccount.getHiscore());
        account.put("scanned", newAccount.getScanned());
        account.put("qrCodes", newAccount.getQRList());
        accountRef.set(account)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Account created!");
                    setAccount(newAccount);
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Account has not been created.");

                    //Should this be here?
                    accountRef.set(newAccount);
                });
    }

    /**
     * Purpose: Add a profileListener for firestore data.
     */
    public void addAccountListener() {
        userUID = currentUser.getUid();
        accountRef = db.collection("Account").document(userUID);
        accountListener = accountRef.addSnapshotListener((accountDocument, error) -> {
            if (error != null) {
                return;
            }
            if (accountDocument != null && accountDocument.exists()) {
                Log.d(TAG, "Account DocumentSnapshot data: " + accountDocument.getData());
                Account savedAccount = accountDocument.toObject(Account.class);
                Log.i(TAG, "savedAccount UID: " + savedAccount.getUserUID());
                Log.i(TAG, "savedAccount Score: " + savedAccount.getScore());
                Log.i(TAG, "savedAccount Hiscore: " + savedAccount.getHiscore());
                Log.i(TAG, "savedAccount Scanned Total: " + savedAccount.getScanned());
                setAccount(savedAccount);    // Update account locally
                Log.d(TAG, savedAccount.getUserUID() + " account snapshot exists!");
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
    }

    /**
     * Purpose: Remove accountListener when player leaves AccountFragment.
     */
    public void removeAccountListener() {
        accountListener.remove();
    }

    /**
     * Purpose: Set/Update account info in SharedPrefs.
     *
     * @param newAccount Account to be set/updated with locally.
     */
    public void setAccount(Account newAccount) {
        accountSPEditor.putString("userUID", newAccount.getUserUID());
        accountSPEditor.putString("score", (newAccount.getScore()).toString());
        accountSPEditor.putString("hiscore", (newAccount.getHiscore()).toString());
        accountSPEditor.putString("scanned", (newAccount.getScanned()).toString());
        accountSPEditor.apply();
    }

    /**
     * Purpose: Return an instance of the account saved locally
     *
     * @return Represents the Account object locally.
     */
    public Account getAccount() {
        String userUID = accountSP.getString("userUID", currentUser.getUid());
        Integer score = Integer.parseInt(accountSP.getString("score", null));
        Integer hiscore = Integer.parseInt(accountSP.getString("hiscore", null));
        Integer scanned = Integer.parseInt(accountSP.getString("scanned", null));
        Account account = new Account(userUID, score, hiscore, scanned);
        return account;
    }

    /**
     * Purpose: Updates the current player's total score on firestore db and locally.
     *
     * @param updatedScore An instance of their updated score.
     */
    public void updateScore(Integer updatedScore) {
        accountRef = accountCollectionRef.document(currentUser.getUid());
        accountRef.update("score", String.valueOf(updatedScore));
    }

    /**
     * Purpose: Updates the current player's total scanned QR codes on firestore db and locally.
     *
     * @param updatedTotal An instance of their updated total scanned QR codes.
     */
    public void updateTotalScanned(Integer updatedTotal) {
        accountRef = accountCollectionRef.document(currentUser.getUid());
        accountRef.update("scanned", String.valueOf(updatedTotal));
    }

    /**
     * Purpose: Updates the current player's high score on firestore db and locally.
     *
     * @param updatedHiscore An instance of their updated high score.
     */
    public void updateHiscore(Integer updatedHiscore) {
        accountRef = accountCollectionRef.document(currentUser.getUid());
        accountRef.update("hiscore", String.valueOf(updatedHiscore));
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
//                        totalScore[0] = Integer.parseInt(data.get("score").toString());
//                        scanned[0] = Integer.parseInt(data.get("scanned").toString());
//                        System.out.println("In docref: "+totalScore[0].toString()+" "+scanned[0].toString());
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
//                        data.put("score", "0");
//                        data.put("scanned", "0");
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
//                                        Log.d(TAG, "Data could not be added. " + e.toString());
//                                    }
//                                });
//                    }
//                } else {
//                    Log.d(TAG,"Get failed with ", task.getException());
//                }
//            }
//        });
//        System.out.println("Outside docref: "+totalScore[0].toString()+" "+scanned[0].toString());
//    }

//    public Account createNewAccount() {
//        System.out.println("In create: "+totalScore[0].toString()+" "+scanned[0].toString());
//        return new Account(userID);
//    }
//}