package com.example.qrscore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrscore.Account;
import com.example.qrscore.QRCode;
import com.example.qrscore.QRCodeAdapter;
import com.example.qrscore.QRDataList;
import com.example.qrscore.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Purpose: This class shows a list of QRCodes that another player owns. Also shows total scanned,
 * total score, and rank. Can click on a QR code to go to a different screen.
 *
 * Outstanding issues:
 * TODO: go to this activity when view profile is clicked on for player
 * TODO: Rank needs to be implemented
 * TODO: Show total score, scanned, username, and rank
 * TODO: Go to QRCodeActivity when QRCode is clicked on
 * TODO: UI testing
 */

public class OtherPlayerAccountActivity extends AppCompatActivity {

    private ListView qrCodesList;
    final String TAG = "OTHER_PLAYER_ACTIVITY";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Account account;
    private QRDataList qrDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_player_account);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String userUID = intent.getStringExtra("userID");

        account = new Account(userUID);

        ArrayList<QRCode> qrCodes = new ArrayList<>();
        QRCodeAdapter qrCodesAdapter = new QRCodeAdapter(this, com.example.qrscore.R.layout.qr_codes_list_content, qrCodes);
        qrCodesList = findViewById(R.id.qr_codes_list_view);
        qrCodesList.setAdapter(qrCodesAdapter);

        // Create textviews
        TextView scannedTextView = findViewById(R.id.scanned_text_view);
        TextView scoreTextView = findViewById(R.id.score_text_view);
        TextView usernameTextView = findViewById(R.id.username_text_view);
        TextView qrCodeTitleTextView = findViewById(R.id.qr_code_title_text_view);
        TextView rankTextView = findViewById(R.id.rank_text_view);

        // Set username textviews
        usernameTextView.setText(userUID);
        qrCodeTitleTextView.setText(userUID + "'s QR Codes");

        DocumentReference profileRef = db.collection("Profile").document(userUID);
        CollectionReference QRCodeRef = db.collection("QRCode");

        //                                                        scoreTextView.setText(qrDataListDocument.getData().get("sumOfScoresScanned").toString());
//                                                        scannedTextView.setText(qrDataListDocument.getData().get("totalQRCodesScanned").toString());
//                                                        rankTextView.setText(qrDataListDocument.getData().get("rank").toString());

        QRCodeRef.whereArrayContains("scanned", userUID).get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               List<DocumentSnapshot> docs = task.getResult().getDocuments();
               if (!docs.isEmpty()) {
                   for (DocumentSnapshot doc: docs) {
                       QRCode qrCode = doc.toObject(QRCode.class);
                       qrCodes.add(qrCode);
                   }
                   qrCodesAdapter.notifyDataSetChanged();
               }
           }
        });

        // GOTO QRCodeActivity when code is clicked on
        qrCodesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // Go to new activity on item click
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(OtherPlayerAccountActivity.this, QRCodeActivity.class);
                intent.putExtra("QR_ID", qrCodesAdapter.getItem(i).getHash());
                startActivity(intent);

            }
        });

    }

}

//        // get account from user UID
//        account.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            QuerySnapshot accountQuery = task.getResult();
//                            if (!accountQuery.isEmpty()) {
//                                Log.d(TAG, "DocumentQuery data: " + accountQuery.getDocuments());
//                                DocumentReference qrDataList = accountQuery.getDocuments().get(0).getDocumentReference("qrDataList");
//
//                                // get qrDataList from account
//                                qrDataList.get()
//                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                if (task.isSuccessful()) {
//                                                    DocumentSnapshot qrDataListDocument = task.getResult();
//                                                    if (qrDataListDocument.exists()) {
//                                                        Log.d(TAG, "DocumentSnapshot data: " + qrDataListDocument.getData());
//                                                        //qrList = qrDataListDocument.toObject(QRDataList.class);
//                                                        scoreTextView.setText(qrDataListDocument.getData().get("sumOfScoresScanned").toString());
//                                                        scannedTextView.setText(qrDataListDocument.getData().get("totalQRCodesScanned").toString());
//                                                        rankTextView.setText(qrDataListDocument.getData().get("rank").toString());
//
//                                                        ArrayList<DocumentReference> qrCodesArray = (ArrayList<DocumentReference>) qrDataListDocument.getData().get("qrCodes");
//
//                                                        // get each QRCode from array
//                                                        for (DocumentReference codeRef : qrCodesArray) {
//                                                            codeRef.get()
//                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                                        @Override
//                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                                            if (task.isSuccessful()) {
//                                                                                DocumentSnapshot document = task.getResult();
//                                                                                if (document.exists()) {
//                                                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                                                                                    QRCode code = document.toObject(QRCode.class);
//
//                                                                                    // Add score to list
//                                                                                    //qrCodesDataList.add(code.getQRScore().toString());
//                                                                                    qrCodesDataList.add(code);
//                                                                                    qrCodesAdapter.notifyDataSetChanged();
//                                                                                } else {
//                                                                                    Log.d(TAG, "No such qr code document");
//                                                                                }
//                                                                            } else {
//                                                                                Log.d(TAG, "get failed with ", task.getException());
//                                                                            }
//                                                                        }});
//
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        });
//                            } else {
//                                Log.d(TAG, "No such account document");
//                            }
//                        } else {
//                            Log.d(TAG, "get failed with ", task.getException());
//                        }
//                    }
//                });
