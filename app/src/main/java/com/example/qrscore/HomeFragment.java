package com.example.qrscore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Purpose: This class is the home fragment which shows some of your player information
 * Shows player's username
 * Shows player's first an last name
 * Shows player's contact info
 * Shows player's QR Code stats
 *
 * Outstanding issues:
 * TODO: Finish Purpose
 * TODO: Implement sort by button
 * TODO: Implement QR Codes list
 * TODO: Connect to Account data in firebase
 * TODO: Get unique device ID
 * TODO: Add header + footer
 * TODO: UI tests
 * TODO: Merge with William's HomeFragment
 */
public class HomeFragment extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener {

    private ArrayAdapter<Comment> commentAdapter;
    private ArrayList<Comment> commentDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment_layout);
        TextView userName = (TextView) findViewById(R.id.username_text_view);
        TextView usernamesQRCodes = (TextView) findViewById(R.id.qr_code_title_text_view);
        TextView myScannedCodes = (TextView) findViewById(R.id.scanned_text_view);
        TextView myQRScore = (TextView) findViewById(R.id.score_text_view);
        TextView myRank = (TextView) findViewById(R.id.rank_text_view);

        Account myAccount = new Account("id1", "Gregg");
        userName.setText(myAccount.profile.getUserName());
        String usernamesQRCodesString = (myAccount.profile.getUserName() + "'s QR Codes");
        usernamesQRCodes.setText(usernamesQRCodesString);
        myScannedCodes.setText(myAccount.qrDataList.getTotalQRCodesScanned().toString());
        myQRScore.setText(myAccount.qrDataList.getSumOfScoresScanned().toString());
        myRank.setText(myAccount.qrDataList.getRank().toString());

        final Button addButton = findViewById(R.id.sort_by_button);
        addButton.setOnClickListener((v) -> {
        });
    }

    @Override
    public void onOkPressed(Comment newComment) {
    }
}