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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;


public class QRCodeActivity extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener{
    private ListView commentList;
    private ArrayAdapter<Comment> commentAdapter;
    private ArrayList<Comment> commentDataList;
    private CollectionReference collectionReference;

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
        Profile profile1 = new Profile("newuser");
        Account account1 = new Account(profile1);
        Player player1 = new Player(account1);
        String[] hasScannedArray = {player1.getUsername()};
        List<String> hasScanned = Arrays.asList(hasScannedArray);
        QRCode code = new QRCode(hasScanned);
        code.setId("QNyyQ2ZLBbCB22lXtTke");
        //code = addToHasScanned(code, player1);
        updateHasScanned(code.getId(), player1);
        loadHasScanned(code.getId());

        // Initialize the DB
        collectionReference = db.collection("Comment");

        commentDataList = new ArrayList<>();
        commentList = findViewById(R.id.comment_list_view);
        commentAdapter = new CommentCustomList(this, commentDataList);
        commentList.setAdapter(commentAdapter);

        // Clicked the add button; adding comments
        final Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener((v) -> {
            new AddCommentFragment().show(getSupportFragmentManager(), "ADD_COMMENT");
        });

        // Reading data and outputting to screen
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException error) {
                commentDataList.clear();
                try {
                    for (QueryDocumentSnapshot doc: value) {
                        String comment = (String) doc.getData().get("Comment");
                        String owner = (String) doc.getData().get("Owner");
                        String qrID = (String) doc.getData().get("qrID");
                        String date = (String) doc.getData().get("Date");
                        commentDataList.add(new Comment(owner, comment, qrID, date));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                commentAdapter.notifyDataSetChanged();
            }
        });

        // TODO: Remove when done testing

        // Displaying the comments when someone presses
        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Comment clickedComment = (Comment) adapterView.getItemAtPosition(pos);
                String commentText = clickedComment.getComment();
                String playerText = clickedComment.getCommenter();

                Bundle args = new Bundle();
                args.putString("COMMENT", commentText);
                args.putString("PLAYER_NAME", playerText);

                DisplayCommentFragment displayCommentFragment = new DisplayCommentFragment();
                displayCommentFragment.setArguments(args);
                displayCommentFragment.show(getSupportFragmentManager(), "DISPLAY_COMMENT");
            }
        });
    }

/*    *//*
     *Adds player to hasScanned for QRCode in firebase
     *TODO: Add this to when a player adds a QR code
     *//*
    public QRCode addToHasScanned(QRCode code, Player player) {

        qrCodeRef.add(code)
*//*                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
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
                }});*//*
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
    }*/


    /*
     * Adds player to hasScanned for existing qrCode
     */
    public void updateHasScanned(String codeID, Player player) {
        DocumentReference id = qrCodeRef.document(codeID);
        id.update("hasScanned", FieldValue.arrayUnion(player.getUsername()));
    }

    public void loadHasScanned(String codeID) {
        qrCodeRef.document(codeID).get()
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
        playerAdapter.notifyDataSetChanged();


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

    /**
     * This adds a new comment to the list and also adds it to our db
     * @param newComment
     *      Comment to add
     */
    @Override
    public void onOkPressed(Comment newComment) {
        commentAdapter.add(newComment);
        HashMap<String, String> commentData = new HashMap<>();

        commentData.put("Comment", newComment.getComment());
        commentData.put("Owner", newComment.getCommenter());
        commentData.put("Date", newComment.getDate());
        commentData.put("qrID", newComment.getID());

        collectionReference.add(commentData);
    }

    /**
     * Returns the commentList to use for our tests
     * @return
     */
    public ListView getCommentList() {
        return commentList;
    }
}