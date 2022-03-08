package com.example.qrscore;

// TODO: As a player, I want to add new QR codes to my account.
//  As a player, I want to see what QR codes I have added to my account.
//  As a player, I want to remove QR codes from my account.
//  As a player, I want to see my highest and lowest scoring QR codes.

// TODO: UI--navbar with "add" button to add QR codes.

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/* Purpose: This class represents an account in the system.
 * Stores the QR codes tied to the account, the device,
 * and the profile.
 *
 * Outstanding issues:
 */
public class Account {
    private static final String TAG = "ACCOUNT";
    private String userID;
    private String device;
    private Profile profile;
    private Stats stats;
    // private Permissions permissions;

    public Account(String userID, String device) {
        this.userID = userID;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference accountReference = db.collection("Account");

        // Checks if the userID exists in the db.
        // From: https://firebase.google.com/docs/firestore/query-data/get-data#get_a_document
        DocumentReference docRef = accountReference.document(userID);
        Task<DocumentSnapshot> task = docRef.get();
        if (task.isSuccessful()) {
            DocumentSnapshot doc = task.getResult();
            if (doc.exists()) {
                // If it does, retrieve data from the account doc in
                // the db to build the account object.
                Map<String, Object> data = doc.getData();
                this.device = data.get("Device").toString();
            } else {
                // Else, initialize everything with default(?) values
                // and then create a new account doc in the db.
                HashMap<String, String> data = new HashMap<>();
                data.put("Device", device);
                accountReference.document(userID)
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

//    public QRCode getHighest() {
//        return stats.getHighscore(qrCodes);
//    }

//    public QRCode getLowest() {
//        return stats.getLowscore(qrCodes);
//    }
}
