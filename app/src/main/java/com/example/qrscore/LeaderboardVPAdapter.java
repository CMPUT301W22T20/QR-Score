package com.example.qrscore;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
// https://www.youtube.com/watch?v=iJpB5ju3tN8
public class LeaderboardVPAdapter extends FragmentStateAdapter {
    public LeaderboardVPAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new LeaderboardQRcodeFragment();
                break;
            case 1:
                fragment = new LeaderboardPlayerFragment();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
