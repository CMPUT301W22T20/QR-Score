/* Purpose: This class represents the QR Code Details Activity.
Shows the location of the QRCode and players that have scanned it.
Shows players that have commented on the QRCode and allows user to click on the players to see their comments.
Allow user to click add comment from this screen.

Outstanding issues:
*/

package com.example.qrscore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO: When Player scans a QR Code, add them to hasScanned in QRCode class

public class QRCodeActivity extends AppCompatActivity{
    private ListView playerList;
    private ArrayAdapter<String> playerAdapter;
    private ArrayList<String> playerDataList;
    final String TAG = "Sample";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference qrCodeRef = db.collection("QRCode");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        // Access a Cloud Firestore instance and get reference to collection
        db = FirebaseFirestore.getInstance();
        final CollectionReference qrCodeCollectionReference = db.collection("QRCode");

        // Create array adapter for players who have scanned a specific QR code
        playerDataList = new ArrayList<String>();
/*        playerDataList.add("user02");
        playerDataList.add("user01");
        playerDataList.add("user03");
        playerDataList.add("user01");
        playerDataList.add("user01");
        playerDataList.add("user40");
        playerDataList.add("user45");*/
        playerList = findViewById(R.id.scanned_by_list_view);
        playerAdapter = new ArrayAdapter<>(this,
                com.example.qrscore.R.layout.scanned_by_content, playerDataList);
        playerList.setAdapter(playerAdapter);

        //test data
        Profile profile1 = new Profile("usernameok");
        Account account1 = new Account(profile1);
        Player player1 = new Player(account1);
        Profile profile2 = new Profile("user001");
        Account account2 = new Account(profile2);
        Player player2 = new Player(account2);
        String[] hasScannedArray = {player1.getUsername()};
        List<String> hasScanned = Arrays.asList(hasScannedArray);
        QRCode code = new QRCode(hasScanned);

        code = addToHasScanned(code, player1);
        loadHasScanned(code);
        //updateHasScanned(code, player2);
    }

    /*
     *Adds player to hasScanned for QRCode in firebase
     *TODO: Add this to when a player adds a QR code
     */
    public QRCode addToHasScanned(QRCode code, Player player) {

        qrCodeRef.add(code)
/*                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            DocumentReference reference = task.getResult();
                            Log.d(TAG, "onSuccess: task was successful");
                            Log.d(TAG, "onSuccess: " + reference.getId());
                            code.setId(reference.getId());  // set id of qrCode
                     }  else {
                            Log.d(TAG, "onFailure: task was NOT successful");
                        }
                }});*/
               .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: task was successful");
                        Log.d(TAG, "onSuccess: " + documentReference.getId());
                        code.setId(documentReference.getId());  // Set the id of the qrCode
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: task was NOT successful");
                    }
                });
        String sid = code.getId();
        return code;
    }

    /*
     * Adds player to hasScanned for existing qrCode
     */
    public void updateHasScanned(QRCode code, Player player) {
        String sid = code.getId();
        DocumentReference id = qrCodeRef.document(code.getId());
        id.update("hasScanned", FieldValue.arrayUnion(player.getUsername()));
    }

    public void loadHasScanned(QRCode code) {
        qrCodeRef.document(code.getId()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        QRCode code = document.toObject(QRCode.class);

                        // Add each player from hasScanned to playerDataList
                        for (String username : code.getHasScanned()) {
                            playerDataList.add(username); }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


              /*  .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        // loop through each document
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            QRCode code = documentSnapshot.toObject(QRCode.class);
                            code.setId(documentSnapshot.getId()); // set QRCode id to documentID

                            // Add each player from hasScanned to playerDataList
                            for (String username : code.getHasScanned()) {
                                playerDataList.add(username);
                            }
                            playerAdapter.notifyDataSetChanged();
                        }

                    }
                });*/
    }


}