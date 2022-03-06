/* Purpose: This class represents the QR Code Details Activity.
Shows the location of the QRCode and players that have scanned it.
Shows players that have commented on the QRCode and allows user to click on the players to see their comments.
Allow user to click add comment from this screen.

Outstanding issues:
*/

package com.example.qrscore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

// TODO: Implement opening up comments
// TODO: Player names
// TODO: Change little icon placeholder

public class QRCodeActivity extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener {
    private ListView commentList;
    private ArrayAdapter<Comment> commentAdapter;
    private ArrayList<Comment> commentDataList;
    private ListView playerList;
    private ArrayAdapter<Player> playerAdapter;
    private ArrayList<Player> playerDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        // Access a Cloud Firestore instance
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Cities");

        commentDataList = new ArrayList<>();
        commentList = findViewById(R.id.comment_list_view);
        commentAdapter = new CommentCustomList(this, commentDataList);
        commentList.setAdapter(commentAdapter);

        final Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener((v) -> {
            new AddCommentFragment().show(getSupportFragmentManager(), "ADD_COMMENT");
        });

        // Create array adapter for players who have scanned a specific QR code
        playerDataList = new ArrayList<>();
        playerList = findViewById(R.id.scanned_by_list_view);
        playerAdapter = new PlayerCustomList(this, playerDataList);
        playerList.setAdapter(playerAdapter);
    }

    @Override
    public void onOkPressed(Comment newComment) {
        commentAdapter.add(newComment);
    }





}