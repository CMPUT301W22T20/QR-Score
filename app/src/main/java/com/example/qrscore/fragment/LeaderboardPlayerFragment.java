package com.example.qrscore.fragment;

import android.os.Bundle;

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
import android.widget.AdapterView;

import com.example.qrscore.Account;
import com.example.qrscore.LeaderboardPlayerRecyclerAdapter;
import com.example.qrscore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

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
        accountListener = accountRef
                .addSnapshotListener((value, error) -> {
                   accounts.clear();
                   for (QueryDocumentSnapshot documentSnapshot: value)  {
                       String userUID = documentSnapshot.getString("UserUID");
                       String score = documentSnapshot.getString("Score");
                       String total = documentSnapshot.getString("Total");
                       accounts.add(new Account(userUID, Integer.parseInt(score), Integer.parseInt(total)));
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
                   String userUID = documentSnapshot.getString("UserUID");
                   String score = documentSnapshot.getString("Score");
                   String total = documentSnapshot.getString("Total");
                   accounts.add(new Account(userUID, Integer.parseInt(score), Integer.parseInt(total)));
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
                if (account.getUserID().toLowerCase().startsWith(username.toLowerCase())) {
                    accountsFiltered.add(account);
                }
            }
            playerRecyclerView.setAdapter(new LeaderboardPlayerRecyclerAdapter(accountsFiltered));
            leaderboardRA.updateList(accountsFiltered);
        }
        leaderboardRA.notifyDataSetChanged();
    }
}