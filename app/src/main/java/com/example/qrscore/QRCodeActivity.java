package com.example.qrscore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

// Comments collection
// Geolocation collection
// QRCode collection
// Photos storage
// Photo collection

public class QRCodeActivity extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener {
    private ListView commentList;
    private ArrayAdapter<Comment> commentAdapter;
    private ArrayList<Comment> commentDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        commentDataList = new ArrayList<>();
        commentList = findViewById(R.id.comment_list_view);
        commentAdapter = new CommentCustomList(this, commentDataList);
        commentList.setAdapter(commentAdapter);

        final Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener((v) -> {
            new AddCommentFragment().show(getSupportFragmentManager(), "ADD_COMMENT");
        });
    }

    @Override
    public void onOkPressed(Comment newComment) {
        commentAdapter.add(newComment);
    }
}