package com.example.qrscore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
 */
public class HomeFragment extends Fragment {

    final String TAG = "HomeFrag";

    private ProfileController profileController;
    private AccountController accountController;
    private QRCodeController qrCodeController;

    private FirebaseFirestore db;

    private CollectionReference qrCollectionRef;
    private CollectionReference accountCollectionRef;
    private CollectionReference QRDataListCollectionRef;
    private CollectionReference profileCollectionRef;

    private DocumentReference qrRef;
    private DocumentReference accountRef;
    private DocumentReference QRDataListRef;
    private DocumentReference profileRef;

    private DocumentSnapshot accountSnapshot;
    private DocumentSnapshot QRDataListSnapshot;
    private DocumentSnapshot profileSnapshot;

    private Account myAccount;
    private String userUID;
    private int score;
    private int total;
    private QRDataList myQRDataList;
    private List<QRCode> qrCodes;

    private HomeFragmentQRCodeRecyclerAdapter HomeFragQRCodeRA;
    private RecyclerView QRCodeRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
//    private CollectionReference qrRef;
    private static Query.Direction direction;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileController = new ProfileController(getContext());
        accountController = new AccountController();
        qrCodeController = new QRCodeController();

        db = FirebaseFirestore.getInstance();
        userUID = profileController.getProfile().getUserUID();
        myAccount = new Account(userUID);
        myAccount.setProfile(profileController.getProfile());
        myQRDataList = new QRDataList();
        myQRDataList.setQRCodes(new ArrayList<QRCode>());
        myAccount.setQrDataList(myQRDataList);

        qrCollectionRef = db.collection("QRCode");
        accountCollectionRef = db.collection("Account");
        profileCollectionRef = db.collection("Profile");
        QRDataListCollectionRef = db.collection("QRDataList");

        accountRef = accountCollectionRef.document(userUID);
        profileRef = profileCollectionRef.document(userUID);
        QRDataListRef = QRDataListCollectionRef.document(userUID);

//        qrRef = db.collection("QRCode");
//        direction = Query.Direction.DESCENDING;
    }

    private void populateData(View view) {

        QRDataListRef.get()
                .addOnCompleteListener(taskQRDataList -> {
                    if (taskQRDataList.isSuccessful()) {
                        DocumentSnapshot qrDataListDocument = taskQRDataList.getResult();
                        if (qrDataListDocument.exists()) {
                            Log.d(TAG, "qrDataListDocument data: " + qrDataListDocument.getData());

                            int total = ((Number) qrDataListDocument.get("totalQRCodesScanned")).intValue();
 
                            ArrayList<DocumentReference> qrCodesArray = (ArrayList<DocumentReference>) qrDataListDocument.getData().get("qrCodes");

                            // get each QRCode from array
                            for (DocumentReference codeRef : qrCodesArray) {
                                codeRef.get()
                                        .addOnCompleteListener(taskQRCodes -> {
                                            if (taskQRCodes.isSuccessful()) {
                                                DocumentSnapshot qrCodesDocument = taskQRCodes.getResult();
                                                if (qrCodesDocument.exists()) {
                                                    Log.d(TAG, "qrCodesdocument data: " + qrCodesDocument.getData());
                                                    QRCode code = qrCodesDocument.toObject(QRCode.class);
                                                    myQRDataList.addQRCode(code);
                                                    myAccount.setQrDataList(myQRDataList);
                                                    Log.i(TAG, "myAccount.getQrDataList().getSumOfScoresScanned(): " + myAccount.getQrDataList().getSumOfScoresScanned());
                                                    Log.i(TAG, "myAccount.getQrDataList().getTotalQRCodesScanned(): " + myAccount.getQrDataList().getTotalQRCodesScanned());
                                                    Log.i(TAG, "myAccount.getScanned(): " + myAccount.getScanned());
                                                    myAccount.setScanned(myAccount.getQrDataList().getSumOfScoresScanned());
                                                    myAccount.setScore(myAccount.getQrDataList().getTotalQRCodesScanned());
                                                    Log.i(TAG, "myAccount.getScanned() after setScore: " + myAccount.getScanned());

                                                    // Instantiate Textview classes to fill layout parameters
                                                    TextView myScannedCodes = (TextView) view.findViewById(R.id.home_fragment_scanned_text_view);
                                                    TextView myQRScore = (TextView) view.findViewById(R.id.home_fragment_score_text_view);
                                                    TextView myRank = (TextView) view.findViewById(R.id.home_fragment_rank_text_view);
                                                    QRCodeRecyclerView = view.findViewById(R.id.home_fragment_qrCode_recycler_view);

                                                    // Set the text of all TextViews
                                                    myScannedCodes.setText(myAccount.getScanned().toString());
                                                    myQRScore.setText(myAccount.getScore().toString());
                                                    myRank.setText("NIL");
                                                    setAdapter();
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Instantiate button
        // TODO: Implement "Sort By" button
        final Button sortByButton = view.findViewById(R.id.home_fragment_sort_by_button);
        sortByButton.setOnClickListener(new sortByButtonOnClickListener(sortByButton, HomeFragQRCodeRA, myAccount));

        // Instantiate Textview classes to fill layout parameters
        TextView userName = (TextView) view.findViewById(R.id.home_fragment_username_text_view);
        TextView usernamesQRCodes = (TextView) view.findViewById(R.id.home_fragment_qr_code_title_text_view);

        String usernamesQRCodesString = (myAccount.getUserID() + "'s QR Codes");

        // Set username TextViews
        usernamesQRCodes.setText(usernamesQRCodesString);
        userName.setText(myAccount.getUserID());

        populateData(view);

        return view;
    }

    /**
     * Purpose: Set the  with current .
     */
    private void setAdapter() {
        HomeFragQRCodeRA = new HomeFragmentQRCodeRecyclerAdapter(myAccount);
        layoutManager = new LinearLayoutManager(getContext());
        QRCodeRecyclerView.setLayoutManager(layoutManager);
        QRCodeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        QRCodeRecyclerView.setAdapter(HomeFragQRCodeRA);
    }

    public class sortByButtonOnClickListener implements View.OnClickListener {

        Button sortByButton;
        HomeFragmentQRCodeRecyclerAdapter HFQRCodeRA;
        Account myAccount;
        QRDataList qrDataListToSort;
        ArrayList<QRCode> qrCodesToSort;

        public sortByButtonOnClickListener(Button sortByButton, HomeFragmentQRCodeRecyclerAdapter homeFragmentQRCodeRecyclerAdapter, Account account) {
            this.myAccount = account;
            this.sortByButton = sortByButton;
            this.HFQRCodeRA = homeFragmentQRCodeRecyclerAdapter;
            this.qrDataListToSort = myAccount.getQrDataList();
            this.qrCodesToSort = qrDataListToSort.getQRCodes();
        }

        @Override
        public void onClick(View view) {
            switch (sortByButton.getText().toString()) {
                case "Highest":
                    sortHighest();
                    sortByButton.setText("Lowest");
                    break;
                case "Lowest":
                    sortLowest();
                    sortByButton.setText("Highest");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + sortByButton.getText());
            }
        }

        private void sortLowest() {
            Collections.sort(qrCodesToSort, new Comparator<QRCode>() {
                @Override
                public int compare(QRCode qrCode, QRCode t1) {
                    return -(qrCode.getQRScore() - t1.getQRScore());
                }
            });

            qrDataListToSort.setQRCodes(qrCodesToSort);
            myAccount.setQrDataList(qrDataListToSort);
            HFQRCodeRA.updateList(myAccount);
        }

        private void sortHighest() {
            Collections.sort(qrCodes, new Comparator<QRCode>() {
                @Override
                public int compare(QRCode qrCode, QRCode t1) {
                    return (qrCode.getQRScore() - t1.getQRScore());
                }
            });

            qrDataListToSort.setQRCodes(qrCodesToSort);
            myAccount.setQrDataList(qrDataListToSort);
            HFQRCodeRA.updateList(myAccount);
        }

    }
}

