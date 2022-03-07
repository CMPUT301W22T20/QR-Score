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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

// TODO: Implement opening up comments
// TODO: Player names
// TODO: Change little icon placeholder

public class QRCodeActivity extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener{
    private ListView commentList;
    private ArrayAdapter<Comment> commentAdapter;
    private ArrayList<Comment> commentDataList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        // Initialize the DB
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Comment");

        commentDataList = new ArrayList<>();
        commentList = findViewById(R.id.comment_list_view);
        commentAdapter = new CommentCustomList(this, commentDataList);
        commentList.setAdapter(commentAdapter);

        // Clicked the add button; adding comments
        final Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener((v) -> {
            new AddCommentFragment().show(getSupportFragmentManager(), "ADD_COMMENT");
        });

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

        // Displaying the comments
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

    @Override
    public void onOkPressed(Comment newComment) {
        commentAdapter.add(newComment);
        HashMap<String, String> commentData = new HashMap<>();
        HashMap<String, String> dateData = new HashMap<>();
        HashMap<String, String> ownerData = new HashMap<>();
        HashMap<String, String> qrIDData = new HashMap<>();

        commentData.put("Comment", newComment.getComment());
        commentData.put("Owner", newComment.getCommenter());
        commentData.put("Date", newComment.getDate());
        commentData.put("qrID", newComment.getID());

        final CollectionReference collectionReference = db.collection("Comment");

        collectionReference
                .document("testComment")
                .set(commentData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    // These are a method which gets executed when the task is succeeded

                        Log.d("Sample", "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d("Sample", "Data could not be added!" + e.toString());
                    }
                });
    }
}