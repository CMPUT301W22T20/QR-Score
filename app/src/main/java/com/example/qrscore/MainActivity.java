package com.example.qrscore;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

// TODO: Implement sort by button
// TODO: Implement QR Codes list
// TODO: Connect to Account data in firebase
// TODO: Get unique device ID
// TODO: Add header + footer


public class MainActivity extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onOkPressed(Comment newComment) {
    }
}