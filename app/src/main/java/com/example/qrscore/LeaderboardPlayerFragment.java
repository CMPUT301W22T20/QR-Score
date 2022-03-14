package com.example.qrscore;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

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

    private ArrayList<Player> players;
    private RecyclerView playerRecyclerView;
    private LeaderboardPlayerRecyclerAdapter leaderboardRA;
    private RecyclerView.LayoutManager layoutManager;
    private TextInputEditText leaderboardSearchPlayerET;

    public LeaderboardPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard_player_fragment, container, false);

        playerRecyclerView = view.findViewById(R.id.leaderboard_player_recyclerview);
//        accountController = new AccountController();
//        players = accountController.getPlayers();
        players = new ArrayList<Player>();
        leaderboardSearchPlayerET = view.findViewById(R.id.leaderboard_username_edit_text);
        leaderboardSearchPlayerET.addTextChangedListener(this);
        populatePlayerArrayList();
        setAdapter();



        return view;
    }

    /**
     * Purpose: Populate the Player ArrayList to display on RecyclerView.
     */
    private void populatePlayerArrayList() {

        players.add(new Player(new Account("4324ifjesjafieoawinvksan")));
        players.add(new Player(new Account("bgdf4ifje324ieoawinvksan")));
        for (int i = 0; i < 20; i++) {
            players.add(new Player(new Account("f342faifnajsnfjowjoias")));
        }
    }

    /**
     * Purpose: Set the LeaderboardPlayerRecyclerAdapter with current players.
     */
    private void setAdapter() {
        leaderboardRA = new LeaderboardPlayerRecyclerAdapter(players);
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

    /**
     * Purpose: Filter the ArrayList of players based on their usernames.
     *
     * @param username
     *      Represents a player's username.
     */
    private void filter(String username) {
        ArrayList<Player> playersFiltered = new ArrayList<Player>();
        for (Player player: players) {
            if (player.getUsername().toLowerCase().contains(username.toLowerCase())) {
                playersFiltered.add(player);
            }
        }
        leaderboardRA.filterList(playersFiltered);
    }
}