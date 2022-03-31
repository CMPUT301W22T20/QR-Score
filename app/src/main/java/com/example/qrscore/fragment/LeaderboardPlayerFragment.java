package com.example.qrscore.fragment;

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

import com.example.qrscore.model.Account;
import com.example.qrscore.controller.LeaderboardPlayerRecyclerAdapter;
import com.example.qrscore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

// https://www.youtube.com/watch?v=__OMnFR-wZU
// https://www.youtube.com/watch?v=OWwOSLfWboY

/**
 * Purpose: Represents a LeaderboardPlayerFragment.
 *
 * Outstanding Issues:
 *  TODO: Implement ranking for US-07.01.01
 *  TODO: UI Testing
 *
 * @author William Liu
 */
public class LeaderboardPlayerFragment extends Fragment implements TextWatcher {
    String TAG = "LeaderboardFragment";
    private ArrayList<Account> accounts;
    private RecyclerView playerRecyclerView;
    private LeaderboardPlayerRecyclerAdapter leaderboardRA;
    private RecyclerView.LayoutManager layoutManager;
    private TextInputEditText leaderboardSearchPlayerET;
    private ListenerRegistration accountListener;

    public LeaderboardPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populatePlayerArrayList();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference accountRef = db.collection("Account");

        //TOP 5 SCORES
        Query accountSortByScore = accountRef.orderBy("totalScore", Query.Direction.DESCENDING).limit(5);
//        Task<QuerySnapshot> accountSortByScoreSnapshot = accountSortByScore.get();
        accountSortByScore.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> accountDocuments = task.getResult().getDocuments();
                    int i = 0;
                    for (DocumentSnapshot accountDocument: accountDocuments) {
//                        System.out.println(accountDocument);
                        String top5score = (String) accountDocument.getData().get("totalScore");
                        String top5uid = (String) accountDocument.getData().get("userUID");
                        Log.i(TAG, "Score Rank " + i + ": " + top5uid + "(" + top5score + ")");
                        // Checking if the accountDocument contains the unique qrID
                        i++;
                    }
                }
            }
        });

        Query accountSortByScanned = accountRef.orderBy("totalScanned", Query.Direction.DESCENDING).limit(5);
        accountSortByScanned.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> accountDocuments = task.getResult().getDocuments();
                    int i = 0;
                    for (DocumentSnapshot accountDocument: accountDocuments) {
//                        System.out.println(accountDocument);
                        String top5scanned = (String) accountDocument.getData().get("totalScanned");
                        String top5uid = (String) accountDocument.getData().get("userUID");
                        Log.i(TAG, "Scanned Rank " + i + ": " + top5uid + "(" + top5scanned + ")");
                        // Checking if the accountDocument contains the unique qrID
                        i++;
                    }
                }
            }
        });

        Query accountSortByHiscore = accountRef.orderBy("hiscore", Query.Direction.DESCENDING).limit(5);
        accountSortByHiscore.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> accountDocuments = task.getResult().getDocuments();
                    int i = 0;
                    for (DocumentSnapshot accountDocument: accountDocuments) {
//                        System.out.println(accountDocument);
                        String top5hiscore = (String) accountDocument.getData().get("hiscore");
                        String top5uid = (String) accountDocument.getData().get("userUID");
                        Log.i(TAG, "Hiscore Rank " + i + ": " + top5uid + "(" + top5hiscore + ")");
                        // Checking if the accountDocument contains the unique qrID
                        i++;
                    }
                }
            }
        });

        accountListener = accountRef
                .addSnapshotListener((value, error) -> {
                   accounts.clear();
                   for (QueryDocumentSnapshot accountDocument: value)  {
                       String userUID = accountDocument.getString("userUID");
                       String totalScore = accountDocument.getString("totalScore");
                       String totalScanned = accountDocument.getString("totalScanned");
                       String hiscore = accountDocument.getString("hiscore");
                       Log.i(TAG, "accountDocument.get(\"score\"): " + accountDocument.get("totalScore"));
                       Log.i(TAG, "userUID: " + userUID);
                       Log.i(TAG, "totalScore: " + totalScore);
                       Log.i(TAG, "totalScanned: " + totalScanned);
                       Log.i(TAG, "hiscore: " + hiscore);

                       accounts.add(new Account(userUID, totalScore, totalScanned, hiscore));
                       leaderboardRA.updateList(accounts);
                   }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard_player_fragment, container, false);

        playerRecyclerView = view.findViewById(R.id.leaderboard_player_recyclerview);
        leaderboardSearchPlayerET = view.findViewById(R.id.leaderboard_username_edit_text);
        leaderboardSearchPlayerET.addTextChangedListener(this);
        setAdapter();
        return view;
    }

    /**
     * Purpose: Populate the Player ArrayList to display on RecyclerView.
     */
    private void populatePlayerArrayList() {
        accounts = new ArrayList<Account>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference accountRef = db.collection("Account");
        accountRef.get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                   String userUID = documentSnapshot.getString("userUID");
                   String totalScore = documentSnapshot.getString("totalScore");
                   String totalScanned = documentSnapshot.getString("totalScanned");
                   String hiscore = documentSnapshot.getString("hiscore");
                   accounts.add(new Account(userUID, totalScore, totalScanned, hiscore));
               }
           }
        });
    }

    /**
     * Purpose: Set the LeaderboardPlayerRecyclerAdapter with current players.
     */
    private void setAdapter() {
        leaderboardRA = new LeaderboardPlayerRecyclerAdapter(accounts);
        layoutManager = new LinearLayoutManager(getContext());
        playerRecyclerView.setLayoutManager(layoutManager);
        playerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        playerRecyclerView.setAdapter(leaderboardRA);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    /**
     * Purpose: Listener to filter the players when user search's players by username.
     *
     * @param editable
     *      Represents the Editable that the player has typed in.
     */
    @Override
    public void afterTextChanged(Editable editable) {
        filter(editable.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accountListener.remove();
    }

    /**
     * Purpose: Filter the ArrayList of players based on their usernames.
     *
     * @param username
     *      Represents a player's username.
     */
    private void filter(String username) {
        ArrayList<Account> accountsFiltered = new ArrayList<Account>();

        if (username.isEmpty()) {
            playerRecyclerView.setAdapter(new LeaderboardPlayerRecyclerAdapter(accounts));
        }
        else {
            for (Account account : accounts) {
                if (account.getUserUID().toLowerCase().startsWith(username.toLowerCase())) {
                    accountsFiltered.add(account);
                }
            }
            playerRecyclerView.setAdapter(new LeaderboardPlayerRecyclerAdapter(accountsFiltered));
            leaderboardRA.updateList(accountsFiltered);
        }
        leaderboardRA.notifyDataSetChanged();
    }
}