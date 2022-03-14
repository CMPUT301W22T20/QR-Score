package com.example.qrscore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.checkerframework.checker.units.qual.A;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private FirebaseFirestore db;
    private CollectionReference qrRef;
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
        qrRef = db.collection("QRCode");
        direction = Query.Direction.DESCENDING;
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

        qrCodes = new ArrayList<>();
        QRListAdapter codeAdapter = new QRListAdapter(getContext(), qrCodes);
        myCodes.setAdapter(codeAdapter);
        codeAdapter.notifyDataSetChanged();

        String uuid = profileController.getProfile().getUserUID();
        qrRef.whereArrayContains("hasScanned", uuid).orderBy("qrscore", direction)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                qrCodes.clear();
                try {
                    for (QueryDocumentSnapshot doc: value) {
                        String hash = (String) doc.getData().get("hash");
                        qrCodes.add(new QRCode(hash));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                codeAdapter.sort(new Comparator<QRCode>() {
                    @Override
                    public int compare(QRCode qrCode, QRCode t1) {
                        return -(qrCode.getQRScore() - t1.getQRScore());
                    }
                });
                codeAdapter.notifyDataSetChanged();
            }
        });

        myCodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String qrID = adapterView.getItemAtPosition(pos).toString();

                Intent intent = new Intent(getContext(), QRCodeActivity.class);
                intent.putExtra("QR_ID", qrID);
                startActivity(intent);
            }
        });


        // Instantiate Account class
        // TODO: Connect to Firebase
        accountController.getNewAccount();
        myAccount = accountController.createNewAccount();

        // Instantiate a String to set a TextView to
        String usernamesQRCodesString = (myAccount.getUserUID() + "'s QR Codes");

        // Set the text of all TextViews
        userName.setText(myAccount.getUserUID());
        usernamesQRCodes.setText(usernamesQRCodesString);
        myScannedCodes.setText(myAccount.getScanned().toString());
        myQRScore.setText(myAccount.getScore().toString());
        myRank.setText(myAccount.getQrDataList().getRank().toString());

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