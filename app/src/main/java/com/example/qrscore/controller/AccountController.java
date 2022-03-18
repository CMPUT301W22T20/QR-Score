package com.example.qrscore.controller;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qrscore.Account;
import com.example.qrscore.QRDataList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Account account;
    private Account savedAccount;
    private QRDataList qrDataList;

    public AccountController() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        accountCollectionRef = db.collection("Account");
        QRDataListCollectionRef = db.collection("QRDataList");
        profileCollectionRef = db.collection("Profile");
        currentUser = firebaseAuth.getCurrentUser();
        userUID = currentUser.getUid();
    }

    /**
     * Purpose: Create an account on firestore and reference to QRDataList and Profile.
     */
    public void createNewAccount() {
        account = new Account(userUID);
        accountRef = accountCollectionRef.document(userUID);
        QRDataListRef = QRDataListCollectionRef.document(userUID);

        HashMap<String, Object> newAccount = new HashMap<>();
        newAccount.put("Profile", db.collection("Profile").document(userUID));
        newAccount.put("UserUID", userUID);
        newAccount.put("Score", "0");
        newAccount.put("Total", "0");
        newAccount.put("QRDataList", db.collection("QRDataList").document(userUID));
        accountRef.set(newAccount)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Account created!");
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Account has not been created.");
                    accountRef.set(account);
                });

        HashMap<String, Object> newQRDataList = new HashMap<>();
        newQRDataList.put("qrCodes", account.getQrDataList().getQRCodes());
        newQRDataList.put("rank", account.getQrDataList().getRank());
        newQRDataList.put("sumOfScoresScanned", account.getQrDataList().getSumOfScoresScanned());
        newQRDataList.put("totalQRCodesScanned", account.getQrDataList().getTotalQRCodesScanned());
        QRDataListRef.set(newQRDataList)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "QRDataList created!");
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "QRDataList has not been created.");
                    QRDataListRef.set(newQRDataList);
                });
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
//                        totalScore[0] = Integer.parseInt(data.get("Score").toString());
//                        scanned[0] = Integer.parseInt(data.get("Total").toString());
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
//                        data.put("Score", "0");
//                        data.put("Total", "0");
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