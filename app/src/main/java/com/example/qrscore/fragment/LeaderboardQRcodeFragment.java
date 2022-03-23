package com.example.qrscore.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrscore.Account;
import com.example.qrscore.LeaderboardPlayerRecyclerAdapter;
import com.example.qrscore.LeaderboardQRCodeRecyclerAdapter;
import com.example.qrscore.QRCode;
import com.example.qrscore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Purpose: Fragment that shows the high scoring QRCOdes.
 *
 * Outstanding issues:
 * TODO: Not all QRCodes are being displayed
 * TODO: Show QRCode rank
 * TODO: Show QRCode score
 * TODO: Order scores based on rank
 */

public class LeaderboardQRcodeFragment extends Fragment {

    private ArrayList<QRCode> qrCodes;
    private RecyclerView qrCodeRecyclerView;
    private LeaderboardQRCodeRecyclerAdapter leaderboardRA;
    private RecyclerView.LayoutManager layoutManager;
    private ListenerRegistration qrCodeListener;

    public LeaderboardQRcodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateQRCodeArrayList();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference qrCodeRef = db.collection("QRCode");
        qrCodeListener = qrCodeRef
            .addSnapshotListener((value, error) -> {
                qrCodes.clear();
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    String hash = documentSnapshot.getString("hash");
                    QRCode code = new QRCode(hash);
                    qrCodes.add(code);
                    leaderboardRA.notifyDataSetChanged();
                }
            });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard_qrcode_fragment, container, false);
        qrCodeRecyclerView = view.findViewById(R.id.leaderboard_qr_code_recyclerview);
        setAdapter();

        Fragment ownerLoginFragment = new OwnerLoginFragment();
        return view;
    }

    /**
     * Purpose: Populate the Player ArrayList to display on RecyclerView.
     */
    private void populateQRCodeArrayList() {
        qrCodes = new ArrayList<QRCode>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference accountRef = db.collection("QRCode");
        accountRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    String hash = documentSnapshot.getString("hash");
                    String score = documentSnapshot.get("qrscore").toString();
                    qrCodes.add(new QRCode(hash));
                    leaderboardRA.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Purpose: Set the LeaderboardPlayerRecyclerAdapter with current players.
     */
    private void setAdapter() {
        leaderboardRA = new LeaderboardQRCodeRecyclerAdapter(qrCodes);
        layoutManager = new LinearLayoutManager(getContext());
        qrCodeRecyclerView.setLayoutManager(layoutManager);
        qrCodeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        qrCodeRecyclerView.setAdapter(leaderboardRA);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        qrCodeListener.remove();
    }
}

