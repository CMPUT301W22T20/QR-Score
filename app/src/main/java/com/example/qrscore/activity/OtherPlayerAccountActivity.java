package com.example.qrscore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.qrscore.R;
import com.example.qrscore.controller.QRCodeAdapter;
import com.example.qrscore.model.QRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
 */

public class OtherPlayerAccountActivity extends AppCompatActivity {

    private ListView qrCodesList;
    final String TAG = "OTHER_PLAYER_ACTIVITY";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference accountRef;
    private DocumentReference profileRef;
    private CollectionReference qrCodeRef;
    private QRCodeAdapter qrCodesAdapter;
    private ArrayList<QRCode> qrCodes;
    private TextView qrCodeTitleTextView;
    private TextView hiscoreTextView;
    private TextView scannedCodesTextView;
    private TextView qrScoreTextView;
    private TextView hiscoreRankTextView;
    private TextView totalScannedRankTextView;
    private TextView totalScoreRankTextView;
    private String totalScore;
    private String totalScanned;
    private String hiscore;
    private String rankTotalScore;
    private String rankTotalScanned;
    private String rankHiscore;

    private String firstName;
    private String lastName;

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
        qrCodesList = findViewById(R.id.qr_codes_list_view);
        qrCodesList.setAdapter(qrCodesAdapter);

        // Instantiate Textview classes to fill layout parameters
        hiscoreTextView = findViewById(R.id.hiscore_text_view);
        scannedCodesTextView = findViewById(R.id.scanned_text_view);
        qrScoreTextView = findViewById(R.id.score_text_view);
        hiscoreRankTextView = findViewById(R.id.hiscore_rank_text_view);
        totalScannedRankTextView = findViewById(R.id.scanned_rank_text_view);
        totalScoreRankTextView = findViewById(R.id.total_score_rank_text_view);
        qrCodeTitleTextView = findViewById(R.id.qr_code_title_text_view);

        // get collection/document references
        accountRef = db.collection("Account").document(userUID);
        qrCodeRef = db.collection("QRCode");
        profileRef = db.collection("Profile").document(userUID);

        firstName = null;
        lastName = null;

        profileRef
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            firstName = doc.getString("firstName");
                            lastName = doc.getString("lastName");

                            if (firstName != null && lastName == null) {
                                qrCodeTitleTextView.setText(firstName + "'s QR Codes" );
                            }
                            else if (firstName != null && lastName != null) {
                                qrCodeTitleTextView.setText(firstName + " " + lastName + "'s QR Codes");
                            }
                            else{
                                qrCodeTitleTextView.setText(userUID + "'s QR Codes");
                            }
                        }
                    }
                });

        loadQRCodes();
    }

    /**
     * Purpose: Populates the listview with the QRCodes of a specific user
     */
    public void loadQRCodes() {
        accountRef.get()    // Get account document
            .addOnCompleteListener(taskAccount -> {
                qrCodesAdapter.clear();
                if (taskAccount.isSuccessful()) {
                    DocumentSnapshot accountDocument = taskAccount.getResult();


                    if (accountDocument.exists()) {
                        Log.d(TAG, "Account DocumentSnapshot data: " + accountDocument.getData());

                        // Get displayed data from firebase
                        totalScore = accountDocument.get("totalScore").toString();
                        totalScanned = accountDocument.get("totalScanned").toString();
                        hiscore = accountDocument.get("hiscore").toString();
                        rankTotalScore = accountDocument.get("rankTotalScore").toString();
                        rankTotalScanned = accountDocument.get("rankTotalScanned").toString();
                        rankHiscore = accountDocument.get("rankHiscore").toString();

                        // Set the text of all TextViews
                        qrScoreTextView.setText(totalScore);
                        scannedCodesTextView.setText(totalScanned);
                        hiscoreTextView.setText(hiscore);
                        totalScoreRankTextView.setText(rankTotalScore);
                        totalScannedRankTextView.setText(rankTotalScanned);
                        hiscoreRankTextView.setText(rankHiscore);

                        ArrayList<String> qrCodesArray = (ArrayList<String>) accountDocument.getData().get("qrCodes");

                        // get each QRCode id
                        for (String codeStr : qrCodesArray) {

                            qrCodeRef.document(codeStr).get()
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
