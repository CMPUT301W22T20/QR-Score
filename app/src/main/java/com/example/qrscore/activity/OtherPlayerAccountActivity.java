package com.example.qrscore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrscore.model.Account;
import com.example.qrscore.model.QRCode;
import com.example.qrscore.controller.QRCodeAdapter;
import com.example.qrscore.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Purpose: This class shows a list of QRCodes that another player owns. Also shows total scanned,
 * total score, and rank. Can click on a QR code to go to a different screen.
 *
 * Outstanding issues:
 * TODO: go to this activity when view profile is clicked on for player
 * TODO: Rank needs to be implemented
 * TODO: Show rank
 * TODO: UI testing
 */

public class OtherPlayerAccountActivity extends AppCompatActivity {

    private ListView qrCodesList;
    final String TAG = "OTHER_PLAYER_ACTIVITY";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Account account;
    private DocumentReference accountRef;
    private CollectionReference codeRef;
    private QRCodeAdapter qrCodesAdapter;
    private ArrayList<QRCode> qrCodes;
    private TextView scannedTextView;
    private TextView scoreTextView;
    private TextView usernameTextView;
    private TextView qrCodeTitleTextView;
    private TextView rankTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_player_account);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String userUID = intent.getStringExtra("userID");

        // Attach adapter for qr_codes_list_view
        qrCodes = new ArrayList<QRCode>();
        qrCodesAdapter = new QRCodeAdapter(this, com.example.qrscore.R.layout.list_items, qrCodes);
        account = new Account(userUID);
        qrCodesList = findViewById(R.id.qr_codes_list_view);
        qrCodesList.setAdapter(qrCodesAdapter);

        // Create textviews
        scannedTextView = findViewById(R.id.scanned_text_view);
        scoreTextView = findViewById(R.id.score_text_view);
        usernameTextView = findViewById(R.id.username_text_view);
        qrCodeTitleTextView = findViewById(R.id.qr_code_title_text_view);
        rankTextView = findViewById(R.id.rank_text_view);

        // Set username textviews
        usernameTextView.setText(userUID);
        qrCodeTitleTextView.setText(userUID + "'s QR Codes");

        // get collection/document references
        accountRef = db.collection("Account").document(userUID);
        codeRef = db.collection("QRCode");

        loadQRCodes();
    }


    /**
     * Purpose: Populates the listview with the QRCodes of a specific user
     */
    public void loadQRCodes() {
        accountRef.get()    // get account document
                .addOnCompleteListener(taskAccount -> {
                    qrCodesAdapter.clear();
                    if (taskAccount.isSuccessful()) {
                        DocumentSnapshot accountDocument = taskAccount.getResult();


                        if (accountDocument.exists()) {
                            Log.d(TAG, "Account DocumentSnapshot data: " + accountDocument.getData());

                            // set textviews
                            String total = (String) accountDocument.get("Total");
                            String score = (String) accountDocument.get("Score");
                            scannedTextView.setText(total);
                            scoreTextView.setText(score);

                            ArrayList<String> qrCodesArray = (ArrayList<String>) accountDocument.getData().get("QRCodes");   // get the QRCodes array

                            // get each QRCode id
                            for (String codeStr : qrCodesArray) {

                                codeRef.document(codeStr).get()
                                        .addOnCompleteListener(taskQRCodes -> {
                                            if (taskQRCodes.isSuccessful()) {
                                                DocumentSnapshot document = taskQRCodes.getResult();

                                                // Get QRCode object and add to adapter
                                                if (document.exists()) {
                                                    Log.d(TAG, "QRCode DocumentSnapshot data: " + document.getData());
                                                    QRCode code = document.toObject(QRCode.class);
                                                    qrCodesAdapter.insert(code, qrCodesAdapter.getCount());
                                                    qrCodesAdapter.notifyDataSetChanged();

                                                } else {
                                                    Log.d(TAG, "No such qr code document");
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", taskQRCodes.getException());
                                            }
                                        });
                            }
                        }
                    }
                });
    }

}
