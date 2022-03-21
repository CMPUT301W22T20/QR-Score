package com.example.qrscore.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrscore.R;

/**
 * Purpose: Fragment that shows the high scoring QRCOdes.
 *
 * Outstanding issues:
 */

public class LeaderboardQRcodeFragment extends Fragment {

    public LeaderboardQRcodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard_qrcode_fragment, container, false);
    }
}