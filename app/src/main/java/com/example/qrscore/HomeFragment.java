package com.example.qrscore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        // Instantiate Textview classes to fill layout parameters
        TextView userName = (TextView) root.findViewById(R.id.username_text_view);
        TextView usernamesQRCodes = (TextView) root.findViewById(R.id.qr_code_title_text_view);
        TextView myScannedCodes = (TextView) root.findViewById(R.id.scanned_text_view);
        TextView myQRScore = (TextView) root.findViewById(R.id.score_text_view);
        TextView myRank = (TextView) root.findViewById(R.id.rank_text_view);

        // Instantiate Account class
        // TODO: Connect to Firebase
        Account myAccount = new Account("id1", "Gregg");

        // Instantiate a String to set a TextView to
        String usernamesQRCodesString = (myAccount.profile.getUserName() + "'s QR Codes");

        // Set the text of all TextViews
        userName.setText(myAccount.profile.getUserName());
        usernamesQRCodes.setText(usernamesQRCodesString);
        myScannedCodes.setText(myAccount.qrDataList.getTotalQRCodesScanned().toString());
        myQRScore.setText(myAccount.qrDataList.getSumOfScoresScanned().toString());
        myRank.setText(myAccount.qrDataList.getRank().toString());

        // Instantiate button
        // TODO:
        final Button sortByButton = root.findViewById(R.id.sort_by_button);
        sortByButton.setOnClickListener((v) -> {
        });

//        setContentView(R.layout.fragment_home);

        return root;
    }

    @Override
    public void onOkPressed(Comment newComment) {

    }
}