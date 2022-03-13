package com.example.qrscore;

<<<<<<< HEAD
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
=======
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
>>>>>>> main

import java.util.ArrayList;

<<<<<<< HEAD
/**
 * Purpose: This class is the main activity
 *
 * Outstanding issues:
 * TODO: Merge with William's MainActivity
 */
public class MainActivity extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener {
=======
    BottomNavigationView bottomNavView;
    HomeFragment homeFragment = new HomeFragment();
    MapFragment mapFragment = new MapFragment();
    ScanFragment scanFragment = new ScanFragment();
    LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
    ProfileFragment profileFragment = new ProfileFragment();

>>>>>>> main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
    }

    @Override
    public void onOkPressed(Comment newComment) {
=======

        // Bottom Nav selector.
        // https://www.youtube.com/watch?v=OV25x3a55pk
        bottomNavView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
        bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_fragment_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
                        return true;
                    case R.id.map_fragment_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mapFragment).commit();
                        return true;
                    case R.id.scan_fragment_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, scanFragment).commit();
                        return true;
                    case R.id.leaderboard_fragment_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, leaderboardFragment).commit();
                        return true;
                    case R.id.profile_fragment_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, profileFragment).commit();
                        return true;
                }
                return false;
            }
        });
>>>>>>> main
    }
}