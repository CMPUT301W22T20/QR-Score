package com.example.qrscore.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscore.R;
import com.example.qrscore.controller.AccountController;
import com.example.qrscore.controller.HomeFragmentQRCodeRecyclerAdapter;
import com.example.qrscore.controller.ProfileController;
import com.example.qrscore.controller.QRCodeAdapter;
import com.example.qrscore.controller.QRCodeController;
import com.example.qrscore.model.Account;
import com.example.qrscore.model.QRCode;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    private CollectionReference qrCodeRef;
    private CollectionReference accountCollectionRef;
    private CollectionReference profileCollectionRef;

    private DocumentReference qrRef;
    private DocumentReference accountRef;
    private DocumentReference profileRef;
    private ListenerRegistration accountListener;

    private DocumentSnapshot accountSnapshot;
    private DocumentSnapshot profileSnapshot;

    private Account myAccount;
    private String userUID;
    private ArrayList<QRCode> qrCodes;
    private QRCodeAdapter qrCodesAdapter;
    private ListView qrCodesList;

    private TextView myHiscoreTextView;
    private TextView myScannedCodesTextView;
    private TextView myQRScoreTextView;
    private TextView myHiscoreRankTextView;
    private TextView myTotalScannedRankTextView;
    private TextView myTotalScoreRankTextView;
    private Button sortByButton;
    private String totalScore;
    private String totalScanned;
    private String hiscore;
    private String rankTotalScore;
    private String rankTotalScanned;
    private String rankHiscore;
    private String firstName;
    private String lastName;
    private String usernamesQRCodesString;

    private HomeFragmentQRCodeRecyclerAdapter HomeFragQRCodeRA;
    private RecyclerView QRCodeRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    //    private CollectionReference qrRef;
    private static Query.Direction direction;
    private ImageButton profileQRButton;
    private TextView actionBarNameTextView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileController = new ProfileController(getContext());
        accountController = new AccountController(getContext());
        qrCodeController = new QRCodeController();

        db = FirebaseFirestore.getInstance();
        userUID = profileController.getProfile().getUserUID();
        firstName = profileController.getProfile().getFirstName();
        lastName = profileController.getProfile().getLastName();

        myAccount = new Account(userUID);
        myAccount.setProfile(profileController.getProfile());

        qrCodeRef = db.collection("QRCode");
        accountCollectionRef = db.collection("Account");
        profileCollectionRef = db.collection("Profile");

        accountRef = accountCollectionRef.document(userUID);
        profileRef = profileCollectionRef.document(userUID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accountListener.remove();
        accountController.removeAccountListener();
    }

    /**
     * Purpose: Populate the data on the homeFragment.
     *
     * @param view
     *      The view to populate on.
     */
    private void populateData(View view) {
        accountRef.get()
                .addOnCompleteListener(taskAccount -> {
                    qrCodesAdapter.clear();
                    if (taskAccount.isSuccessful()) {
                        DocumentSnapshot accountDocument = taskAccount.getResult();

                        if (accountDocument.exists()) {
                            Log.d(TAG, "Account DocumentSnapshot data: " + accountDocument.getData());

                            setStats(accountDocument);

                            ArrayList<String> qrCodeHashes = (ArrayList<String>) accountDocument.getData().get("qrCodes");
                            ArrayList<QRCode> qrCodesArray = new ArrayList<>();

                            for (String qrCodeHash: qrCodeHashes) {
                                System.out.println(qrCodeHash);
                                qrCodesArray.add(new QRCode(qrCodeHash));
                            }
                            myAccount.setQRCodesList(qrCodesArray);

                            setAdapter();
                        }
                    }
                    qrCodes = (ArrayList<QRCode>) myAccount.getQRCodesList();
                    if (qrCodes.size() > 1) {
                        Collections.sort(qrCodes, new Comparator<QRCode>() {
                            @Override
                            public int compare(QRCode qrCode, QRCode t1) {
                                return -(Integer.parseInt(qrCode.getQRScore()) - Integer.parseInt(t1.getQRScore()));
                            }
                        });

                        HomeFragQRCodeRA.updateList(myAccount);
                    }
                    sortByButton.setOnClickListener(new sortByButtonOnClickListener(sortByButton, HomeFragQRCodeRA, myAccount));
                });
    }

    /**
     * Purpose: Update the player's stats with data from firebase
     * @param accountDocument
     *      an instance of the account document
     */
    public void setStats(DocumentSnapshot accountDocument) {
        // Get displayed data from firebase
        totalScore = accountDocument.get("totalScore").toString();
        totalScanned = accountDocument.get("totalScanned").toString();
        hiscore = accountDocument.get("hiscore").toString();
        rankTotalScore = accountDocument.get("rankTotalScore").toString();
        rankTotalScanned = accountDocument.get("rankTotalScanned").toString();
        rankHiscore = accountDocument.get("rankHiscore").toString();

        // Set the text of all TextViews
        myQRScoreTextView.setText(totalScore);
        myScannedCodesTextView.setText(totalScanned);
        myHiscoreTextView.setText(hiscore);
        myTotalScoreRankTextView.setText(rankTotalScore);
        myTotalScannedRankTextView.setText(rankTotalScanned);
        myHiscoreRankTextView.setText(rankHiscore);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Get context
        Context cont;
        cont = getActivity();

        // Attach adapter for qr_codes_list_view
        qrCodes = new ArrayList<QRCode>();
        assert cont != null;
        qrCodesAdapter = new QRCodeAdapter(cont, com.example.qrscore.R.layout.list_items, qrCodes);
        // My QR Code list adapter
        qrCodesList = view.findViewById(R.id.qr_codes_list_view);
        qrCodesList.setAdapter(qrCodesAdapter);

        // **This class is being constructed before the HFRA is initialized.
        sortByButton = view.findViewById(R.id.home_fragment_sort_by_button);

        // Instantiate Textview classes to fill layout parameters
        TextView usernamesQRCodes = (TextView) view.findViewById(R.id.home_fragment_qr_code_title_text_view);

        usernamesQRCodesString = profileController.getProfile().getUserUID();

        // Set username TextViews
        if (firstName != null && lastName == null) {
            usernamesQRCodes.setText(firstName + "'s QR Codes" );
        }
        else if (firstName != null && lastName != null) {
            usernamesQRCodes.setText(firstName + " " + lastName + "'s QR Codes");
        }
        else{
            usernamesQRCodes.setText(usernamesQRCodesString + "'s QR Codes");
        }

        profileQRButton = view.findViewById(R.id.home_fragment_actionbar_qr_code);
        profileQRButton.setOnClickListener(new profileGeneratorButtonListener(userUID));
        actionBarNameTextView = view.findViewById(R.id.home_fragment_actionbar_textview);

        // Instantiate Textview classes to fill layout parameters
        myHiscoreTextView = (TextView) view.findViewById(R.id.hiscore_text_view);
        myScannedCodesTextView = (TextView) view.findViewById(R.id.scanned_text_view);
        myQRScoreTextView = (TextView) view.findViewById(R.id.score_text_view);
        myHiscoreRankTextView = (TextView) view.findViewById(R.id.hiscore_rank_text_view);
        myTotalScannedRankTextView = (TextView) view.findViewById(R.id.scanned_rank_text_view);
        myTotalScoreRankTextView = (TextView) view.findViewById(R.id.total_score_rank_text_view);
        QRCodeRecyclerView = view.findViewById(R.id.home_fragment_qrCode_recycler_view);

        populateData(view);
        accountController.addAccountListener();
        accountListener = accountRef.addSnapshotListener((accountDocument, error) -> {
            if (error != null) {
                return;
            }
            if (accountDocument != null && accountDocument.exists()) {
                setStats(accountDocument);
            }
        });

        return view;
    }

    /**
     * Purpose: Set RecyclerAdapter with QRCodes
     */
    private void setAdapter() {
        HomeFragQRCodeRA = new HomeFragmentQRCodeRecyclerAdapter(myAccount);
        layoutManager = new LinearLayoutManager(getContext());
        QRCodeRecyclerView.setLayoutManager(layoutManager);
        QRCodeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        QRCodeRecyclerView.setAdapter(HomeFragQRCodeRA);
    }

    /**
     * Purpose: Implement the SortByListener to sort the QRCodes based on score.
     */
    public class sortByButtonOnClickListener implements View.OnClickListener {

        Button sortByButton;
        HomeFragmentQRCodeRecyclerAdapter HFQRCodeRA;
        Account myAccount;
        ArrayList<QRCode> qrCodesToSort;

        public sortByButtonOnClickListener(Button sortByButton, HomeFragmentQRCodeRecyclerAdapter homeFragmentQRCodeRecyclerAdapter, Account account) {
            this.myAccount = account;
            this.sortByButton = sortByButton;
            this.HFQRCodeRA = homeFragmentQRCodeRecyclerAdapter;
            this.qrCodesToSort = (ArrayList<QRCode>) account.getQRCodesList();
        }

        @Override
        public void onClick(View view) {
            switch (sortByButton.getText().toString()) {
                case "Highest":
                    sortLowest();
                    sortByButton.setText("Lowest");
                    break;
                case "Lowest":
                    sortHighest();
                    sortByButton.setText("Highest");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + sortByButton.getText());
            }
        }

        public void setAdapter(HomeFragmentQRCodeRecyclerAdapter adapter) {
            this.HFQRCodeRA = adapter;
        }

        /**
         * Purpose: Sort the QRCodes from lowest score to highest.
         */
        private void sortLowest() {
            if (qrCodes.size() > 1) {
                Collections.sort(qrCodes, new Comparator<QRCode>() {
                    @Override
                    public int compare(QRCode qrCode, QRCode t1) {
                        return (Integer.parseInt(qrCode.getQRScore()) - Integer.parseInt(t1.getQRScore()));
                    }
                });

                HFQRCodeRA.updateList(myAccount);
            }
        }

        /**
         * Purpose: Sort the QRCodes from highest score to lowest.
         */
        private void sortHighest() {
            if (qrCodes.size() > 1) { //Why size 0?
                Collections.sort(qrCodes, new Comparator<QRCode>() {
                    @Override
                    public int compare(QRCode qrCode, QRCode t1) {
                        return -(Integer.parseInt(qrCode.getQRScore()) - Integer.parseInt(t1.getQRScore()));
                    }
                });

                HFQRCodeRA.updateList(myAccount);
            }
        }
    }

    /**
     * Purpose: To generate the player's QR Code to allow other to view their game status.
     */
    private class profileGeneratorButtonListener implements View.OnClickListener {
        String userUID;

        public profileGeneratorButtonListener(String userUID) {
            this.userUID = userUID;
        }

        @Override
        public void onClick(View view) {
            openQRDialog();
        }

        private void openQRDialog() {
            // https://stackoverflow.com/a/15459259
            Bundle args = new Bundle();
            args.putString("email", "");
            args.putString("userUID", userUID);
            args.putBoolean("login", false);

            QRGeneratorDialog qrGeneratorDialog = new QRGeneratorDialog();
            qrGeneratorDialog.setArguments(args);
            qrGeneratorDialog.show(getChildFragmentManager(), "QR Dialog");
        }
    }
}

