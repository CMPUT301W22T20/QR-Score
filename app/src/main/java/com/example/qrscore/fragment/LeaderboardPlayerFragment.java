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
import com.example.qrscore.model.Account;
import com.example.qrscore.controller.LeaderboardPlayerRecyclerAdapter;
import com.example.qrscore.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Purpose: Represents a LeaderboardPlayerFragment.
 *
 * Resources:
 * - https://www.youtube.com/watch?v=__OMnFR-wZU
 * - https://www.youtube.com/watch?v=OWwOSLfWboY
 *
 * Outstanding Issues:
 *  TODO: Implement ranking for US-07.01.01
 *
 * @author William Liu
 */
public class LeaderboardPlayerFragment extends Fragment implements TextWatcher {
    String TAG = "LeaderboardPlayerFragment";
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
        accounts.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference accountRef = db.collection("Account");
        accountListener = accountRef
                .addSnapshotListener((value, error) -> {
                    accounts.clear();
                    for (QueryDocumentSnapshot documentSnapshot: value)  {
                        String userUID = documentSnapshot.getString("userUID");
                        String score = documentSnapshot.getString("totalScore");
                        String rankTotalScanned = documentSnapshot.getString("rankTotalScore");
                        accounts.add(new Account(userUID, score, rankTotalScanned));
                        sortAccounts();
                        leaderboardRA.updateList(accounts);
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

    /**
     * Purpose: To sort the accounts based on total score from highest to lowest.
     */
    private void sortAccounts() {
        Collections.sort(accounts, new Comparator<Account>() {
            @Override
            public int compare(Account account, Account t1) {
                return -(Integer.parseInt(account.getTotalScore()) - Integer.parseInt(t1.getTotalScore()));
            }
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        accountListener.remove();
    }
}