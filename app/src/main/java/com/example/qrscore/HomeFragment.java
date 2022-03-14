package com.example.qrscore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.checkerframework.checker.units.qual.A;

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
public class HomeFragment extends Fragment implements AddCommentFragment.OnFragmentInteractionListener {

    private ArrayAdapter<Comment> commentAdapter;
    private ArrayList<Comment> commentDataList;
    private ProfileController profileController;
    private AccountController accountController;
    private Account myAccount;
    private QRCodeController qrCodeController;
    private ArrayList<QRCode> qrCodes;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileController = new ProfileController(getContext());
        accountController = new AccountController();
        qrCodeController = new QRCodeController();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Instantiate Textview classes to fill layout parameters
        TextView userName = (TextView) root.findViewById(R.id.home_fragment_username_text_view);
        TextView usernamesQRCodes = (TextView) root.findViewById(R.id.home_fragment_qr_code_title_text_view);
        TextView myScannedCodes = (TextView) root.findViewById(R.id.home_fragment_scanned_text_view);
        TextView myQRScore = (TextView) root.findViewById(R.id.home_fragment_score_text_view);
        TextView myRank = (TextView) root.findViewById(R.id.home_fragment_rank_text_view);
        ListView myCodes = root.findViewById(R.id.home_fragment_codes_list_view);



        String uuid = profileController.getProfile().getUserUID();
        qrCodeController.getPlayerQRs(new QRCodeCallbackList() {
            @Override
            public void onCallback(ArrayList<QRCode> qrCodesList) {
                System.out.println("55555555555555555555555555");
                qrCodes = qrCodesList;
                System.out.println(qrCodes.size());
                QRListAdapter codeAdapter = new QRListAdapter(getContext(), qrCodes);
                codeAdapter.sort(new Comparator<QRCode>() {
                    @Override
                    public int compare(QRCode qrCode, QRCode t1) {
                        return -(qrCode.getQRScore() - t1.getQRScore());
                    }
                });
                myCodes.setAdapter(codeAdapter);
                codeAdapter.notifyDataSetChanged();
            }
        }, uuid);




        // Instantiate Account class
        // TODO: Connect to Firebase
        accountController.getNewAccount();
        myAccount = accountController.createNewAccount();

        // Instantiate a String to set a TextView to
        String usernamesQRCodesString = (myAccount.profile.getUserUID() + "'s QR Codes");

        // Set the text of all TextViews
        userName.setText(myAccount.profile.getUserUID());
        usernamesQRCodes.setText(usernamesQRCodesString);
        myScannedCodes.setText(myAccount.getScanned().toString());
        myQRScore.setText(myAccount.getTotalScore().toString());
        myRank.setText(myAccount.qrDataList.getRank().toString());

        // Instantiate button
        // TODO: Implement "Sort By" button
        final Button sortByButton = root.findViewById(R.id.home_fragment_sort_by_button);
        sortByButton.setOnClickListener((v) -> {
            QRListAdapter newAdapter = new QRListAdapter(getContext(), qrCodes);
            switch (sortByButton.getText().toString()) {
                case "Highest":
                    // From stackoverflow
                    // https://stackoverflow.com/a/8920348
                    // https://stackoverflow.com/users/844882/alex-lockwood
                    newAdapter.sort(new Comparator<QRCode>() {
                        @Override
                        public int compare(QRCode qrCode, QRCode t1) {
                            return (qrCode.getQRScore() - t1.getQRScore());
                        }
                    });
                    sortByButton.setText("Lowest");
                    break;
                case "Lowest":
                    newAdapter.sort(new Comparator<QRCode>() {
                        @Override
                        public int compare(QRCode qrCode, QRCode t1) {
                            return -(qrCode.getQRScore() - t1.getQRScore());
                        }
                    });
                    sortByButton.setText("Highest");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + sortByButton.getText());
            }
            myCodes.setAdapter(newAdapter);
            newAdapter.notifyDataSetChanged();
        });

        return root;
    }

    @Override
    public void onOkPressed(Comment newComment) {

    }
}