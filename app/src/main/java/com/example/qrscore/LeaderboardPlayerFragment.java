package com.example.qrscore;

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

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

// https://www.youtube.com/watch?v=__OMnFR-wZU
// https://www.youtube.com/watch?v=OWwOSLfWboY
public class LeaderboardPlayerFragment extends Fragment implements TextWatcher{

    private ArrayList<Player> players;
    private RecyclerView playerRecyclerView;
    private LeaderboardRecyclerAdapter leaderboardRA;
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
        players = new ArrayList<Player>();
        leaderboardSearchPlayerET = view.findViewById(R.id.leaderboard_username_edit_text);
        leaderboardSearchPlayerET.addTextChangedListener(this);
        populatePlayerArrayList();
        setAdapter();
        return view;
    }

    private void populatePlayerArrayList() {
        players.add(new Player(new Account(new Profile("4324ifjesjafieoawinvksan"), new Stats())));
        players.add(new Player(new Account(new Profile("bgdf4ifje324ieoawinvksan"), new Stats())));
        for (int i = 0; i < 20; i++) {
            players.add(new Player(new Account(new Profile("f342faifnajsnfjowjoias"), new Stats())));
        }
    }

    private void setAdapter() {
        leaderboardRA = new LeaderboardRecyclerAdapter(players);
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

    @Override
    public void afterTextChanged(Editable editable) {
        filter(editable.toString());
    }

    private void filter(String text) {
        ArrayList<Player> playersFiltered = new ArrayList<Player>();
        for (Player player: players) {
            if (player.getUsername().toLowerCase().contains(text.toLowerCase())) {
                playersFiltered.add(player);
            }
        }
        leaderboardRA.filterList(playersFiltered);
    }
}