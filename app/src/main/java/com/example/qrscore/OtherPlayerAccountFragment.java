package com.example.qrscore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.Reference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

// TODO: go to this activity when view profile is clicked on for player

public class OtherPlayerAccountFragment extends Fragment {

    private ListView qrCodesList;
    private ArrayAdapter<String> qrCodesAdapter;
    private ArrayList<String> qrCodesDataList;
    final String TAG = "Sample";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference profileRef = db.collection("Profile");
    private CollectionReference accountRef = db.collection("Account");
    private CollectionReference qrDataListRef = db.collection("QRDataList");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OtherPlayerAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherPlayerAccountFragment newInstance(String param1, String param2) {
        OtherPlayerAccountFragment fragment = new OtherPlayerAccountFragment();
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

        String userUID = "008pIplmeCdA35SkXKh2B2fL0B82";
        DocumentReference profileRef = db.collection("Profile").document(userUID);
        loadQRCodes(profileRef);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);

    }

    /**
     * Loads who has scanned a QRCode from firebase and outputs to screen
     * @param profileRef
     *          username of the player
     */
    public void loadQRCodes(DocumentReference profileRef) {

        Query account = db.collection("Account").whereEqualTo("Profile", profileRef);

        // get account from user UID
        account.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot accountQuery = task.getResult();
                            if (!accountQuery.isEmpty()) {
                                Log.d(TAG, "DocumentQuery data: " + accountQuery.getDocuments());
                                String qrDataListPath = accountQuery.getDocuments().get(0).getString("qrDataList");
                                DocumentReference qrDataList = db.collection("QRDataList").document(qrDataListPath);

                                // get qrDataList from account
                                qrDataList.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot qrDataListDocument = task.getResult();
                                                    if (qrDataListDocument.exists()) {
                                                        Log.d(TAG, "DocumentSnapshot data: " + qrDataListDocument.getData());
                                                        QRDataList qrDataList = qrDataListDocument.toObject(QRDataList.class);

                                                        // Add each QR Code from qrDataList to list
                                                        for (QRCode code : qrDataList.getQRCodes()) {
                                                            qrCodesDataList.add(code.getQRScore().toString());
                                                        }
                                                    } else {
                                                        Log.d(TAG, "No such qrDataList document");
                                                    }
                                                }
                                            }
                                        });
                            } else {
                                Log.d(TAG, "No such account document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                        qrCodesAdapter.notifyDataSetChanged();
                    }
                });
    }
}