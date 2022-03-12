package com.example.qrscore;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Purpose: This class is the main activity
 *
 * Outstanding issues:
 * TODO: Merge with William's MainActivity
 */
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