package com.example.qrscore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccountController {
    private final String TAG = "ACCOUNTCONTROLLER";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private String userID;
    private final Integer[] totalScore = {50};
    private final Integer[] scanned = {50};

    public AccountController() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Account");
        currentUser = firebaseAuth.getCurrentUser();
        userID = currentUser.getUid();
    }

    public void getNewAccount() {
        collectionReference = db.collection("Account");

        // Checks if the userID exists in the db.
        // From: https://firebase.google.com/docs/firestore/query-data/get-data#get_a_document
        DocumentReference docRef = collectionReference.document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        // If it does, retrieve data from the account doc in
                        // the db to build the account object.
                        Map<String, Object> data = doc.getData();
                        totalScore[0] = Integer.parseInt(data.get("Score").toString());
                        scanned[0] = Integer.parseInt(data.get("Total").toString());
                    } else {
                        // Else, initialize everything with default(?) values
                        // and then create a new account doc in the db.
                        HashMap<String, Object> data = new HashMap<>();

                        // From stackoverflow
                        // https://stackoverflow.com/questions/51292378/how-do-you-insert-a-reference-value-into-firestore
                        // https://stackoverflow.com/users/3095195/trojek
                        data.put("Profile", db.collection("Profile").document(userID));

                        data.put("Score", "0");
                        data.put("Total", "0");
                        collectionReference.document(userID)
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Data added successfully.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Data could not be added. " + e.toString());
                                    }
                                });
                    }
                } else {
                    Log.d(TAG,"Get failed with ", task.getException());
                }
            }
        });
    }

    public Account createNewAccount() {
        return new Account(userID);
    }
}