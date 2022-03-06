package com.example.qrscore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

// TODO: Implement opening up comments
// TODO: Player names
// TODO: Change little icon placeholder

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