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

// TODO: Implement sort by button
// TODO: Implement QR Codes list
// TODO: Connect to Account data in firebase
// TODO: Get unique device ID
// TODO: Add header + footer


public class HomeFragment extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener {
    private TextView userName;
    private TextView qrCodeTitle;
    private TextView myScannedCodes;
    private TextView myQRScore;
    private TextView myRank;
    private Account myAccount;
    private String usernamesQRCodes;

    private ArrayAdapter<Comment> commentAdapter;
    private ArrayList<Comment> commentDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment_layout);
        userName = (TextView) findViewById(R.id.username_text_view);
        qrCodeTitle = (TextView) findViewById(R.id.qr_code_title_text_view);
        myScannedCodes = (TextView) findViewById(R.id.scanned_text_view);
        myQRScore = (TextView) findViewById(R.id.score_text_view);
        myRank = (TextView) findViewById(R.id.rank_text_view);

        myAccount = new Account("id1", "Gregg");
        userName.setText(myAccount.profile.getUserName());
        usernamesQRCodes = (myAccount.profile.getUserName() + "'s QR Codes");
        qrCodeTitle.setText(usernamesQRCodes);
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