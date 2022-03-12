package com.example.qrscore;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * Purpose: This class is used to represent the leaderboard fragment.
 *
 * Outstanding Issues:
 *
 *  @author William Liu/
 */
public class LeaderboardFragment extends Fragment {
    TabLayout leaderboardTabLayout;
    ViewPager2 viewPager;

    public LeaderboardFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        leaderboardTabLayout = view.findViewById(R.id.leaderboard_tabLayout);
        viewPager = view.findViewById(R.id.leaderboard_viewPager);
        LeaderboardVPAdapter adapter = new LeaderboardVPAdapter(getActivity());
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(leaderboardTabLayout, viewPager, (tab, position) -> {
            setIconAndText(tab, position);
        }).attach();
        return view;
    }

    private void setIconAndText(TabLayout.Tab tab, int position) {
        if (position == 0) {
            tab.setIcon(R.drawable.ic_qr_code);
            tab.setText("QR Code");
        }
        else {
            tab.setIcon(R.drawable.ic_profile);
            tab.setText("Player");
        }
    }

}