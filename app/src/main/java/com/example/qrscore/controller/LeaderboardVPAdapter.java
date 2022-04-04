package com.example.qrscore.controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.qrscore.fragment.LeaderboardPlayerFragment;
import com.example.qrscore.fragment.LeaderboardQRcodeFragment;

/**
 * Purpose: Represents a ViewPager2 Adapter for the TabLayout in the LeaderboardFragment.
 *
 * Outstanding Issues:
 *
 * @author: William Liu
 */
// https://www.youtube.com/watch?v=iJpB5ju3tN8
public class LeaderboardVPAdapter extends FragmentStateAdapter {
    public LeaderboardVPAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * Purpose: Creates and returns a Fragments. (Fragment Manager)
     *
     * @param position
     *      Position of tab selected.
     *
     * @return
     *      Represents the fragment of the tab selected.
     */
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

    /**
     * Purpose: Returns the number of tabs.
     *
     * @return
     *      The number of tabs in the leaderboard TabLayout.
     */
    @Override
    public int getItemCount() {
        return 2;
    }
}
