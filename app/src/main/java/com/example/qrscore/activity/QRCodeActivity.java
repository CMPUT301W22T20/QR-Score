package com.example.qrscore.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.qrscore.model.Comment;
import com.example.qrscore.model.CommentCustomList;
import com.example.qrscore.controller.PhotoCallback;
import com.example.qrscore.R;
import com.example.qrscore.controller.PhotoController;
import com.example.qrscore.controller.ProfileController;
import com.example.qrscore.fragment.AddCommentFragment;
import com.example.qrscore.fragment.DisplayCommentFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Purpose:
 * - This class represents the QR Code Details Activity.
 * - Shows the location of the QRCode and players that have scanned it.
 * - Shows players that have commented on the QRCode and allows user to click on the players to see their comments.
 * - Allow user to click add comment from this screen.
 *
 * Outstanding issues:
 */
public class QRCodeActivity extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener {
    private ListView commentList;
    private ArrayAdapter<Comment> commentAdapter;
    private ArrayList<Comment> commentDataList;
    private CollectionReference collectionReference;

    private ProfileController profileController;
    private PhotoController photoController;
    private String qrID;
    private TextView geoText;

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

        photoController = new PhotoController(this);
        profileController = new ProfileController(this);
        String uuid = profileController.getProfile().getUserUID();

        // Create array adapter for players who have scanned a specific QR code
        playerDataList = new ArrayList<String>();
        playerList = findViewById(R.id.scanned_by_list_view);
        playerAdapter = new ArrayAdapter<>(this, com.example.qrscore.R.layout.scanned_by_content, playerDataList);
        playerList.setAdapter(playerAdapter);

        // Initialize the DB
        collectionReference = db.collection("Comment");

        // Initialize comments list
        commentDataList = new ArrayList<>();
        commentList = findViewById(R.id.comment_list_view);
        commentAdapter = new CommentCustomList(this, commentDataList);
        commentList.setAdapter(commentAdapter);

        geoText = findViewById(R.id.geolocation_text_view);
        ImageView imageView = findViewById(R.id.qr_image_view);

        Intent intent = getIntent();
        qrID = (String) intent.getExtras().get("QR_ID");
        loadHasScanned(qrID);

        photoController.downloadPhoto(qrID, uuid, new PhotoCallback() {
            @Override
            public void onCallback(Uri downloadURL) {
                Glide.with(imageView)
                        .load(downloadURL)
                        .centerCrop()
                        .placeholder(R.drawable.ic_baseline_qr_code_24)
                        .into(imageView);
            }
        });

        // Getting all locations where the uuids match
        db.collection("Location").whereArrayContains("uuids", uuid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    for (DocumentSnapshot doc: docs) {
                        System.out.println(doc);
                        ArrayList<String> qrIDs = (ArrayList<String>) doc.getData().get("qrIDs");

                        // Checking if the doc contains the unique qrID
                        if (qrIDs.contains(qrID)) {
                            GeoPoint geoPoint = (GeoPoint) doc.get("geoPoint");
                            String text = geoPoint.getLatitude() + ", " + geoPoint.getLongitude();
                            geoText.setText(text);
                            break;
                        }
                    }
                }
            }
        });

        loadHasScanned(qrID);

        // Clicked the add button; adding comments
        final Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener((v) -> {
            Bundle bundle = new Bundle();
            bundle.putString("QR_ID_TO_FRAGMENT", qrID);
            AddCommentFragment addCommentFragment = new AddCommentFragment();
            addCommentFragment.setArguments(bundle);
            addCommentFragment.show(getSupportFragmentManager(), "ADD_COMMENT");
        });

        // Reading data and outputting to screen
        collectionReference.whereEqualTo("qrID", qrID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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

        playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // TODO: Goto player profiles when clicked
            }
        });

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
     * Purpose: Loads who has scanned a QRCode from firebase and outputs to screen.
     *
     * @param codeID
     *          firebase document ID of the QRCode
     */
    public void loadHasScanned(String codeID) {
        qrCodeRef.document(codeID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                playerDataList.clear();
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                ArrayList<String> userIDs = (ArrayList<String>) document.getData().get("hasScanned");

                                // Add each player from hasScanned to playerDataList
                                playerDataList.addAll(userIDs);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                        playerAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * Purpose: This adds a new comment to the list and also adds it to our db.
     *
     * @param newComment
     *      Comment to add.
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
     *      List view of the comments.
     */
    public ListView getCommentList() {
        return commentList;
    }
}