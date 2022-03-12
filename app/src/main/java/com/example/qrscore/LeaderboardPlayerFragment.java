package com.example.qrscore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

// https://www.youtube.com/watch?v=__OMnFR-wZU
public class LeaderboardPlayerFragment extends Fragment {

    private ArrayList<Player> players;
    private RecyclerView playerRecyclerView;

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
        populatePlayerArrayList();
        setAdapter();
        return view;
    }

    private void populatePlayerArrayList() {
        for (int i = 0; i < 20; i++) {
            players.add(new Player(new Account(new Profile("f342faifnajsnfjowjoias"), new Stats())));
        }
    }

    private void setAdapter() {
        LeaderboardRecyclerAdapter leaderboardRA = new LeaderboardRecyclerAdapter(players);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        playerRecyclerView.setLayoutManager(layoutManager);
        playerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        playerRecyclerView.setAdapter(leaderboardRA);
    }
}