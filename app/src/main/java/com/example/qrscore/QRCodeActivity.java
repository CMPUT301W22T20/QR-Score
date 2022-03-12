package com.example.qrscore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

<<<<<<< HEAD
/**
 * Purpose: This class is the QR Code activity
 *
 * Outstanding issues:
 * TODO: Finish Purpose
 * TODO: Implement opening up comments
 * TODO: Player names
 * TODO: Change little icon placeholder
 * TODO: UI tests
 */
public class QRCodeActivity extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener {
=======
// TODO: Player names
// TODO: Change little icon placeholder

public class QRCodeActivity extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener{
>>>>>>> main
    private ListView commentList;
    private ArrayAdapter<Comment> commentAdapter;
    private ArrayList<Comment> commentDataList;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        // Initialize the DB
        db = FirebaseFirestore.getInstance();
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