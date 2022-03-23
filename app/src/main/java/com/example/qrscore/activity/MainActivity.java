package com.example.qrscore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.os.Bundle;

import android.view.MenuItem;

import com.example.qrscore.model.Account;
import com.example.qrscore.R;
import com.example.qrscore.fragment.HomeFragment;
import com.example.qrscore.fragment.LeaderboardFragment;
import com.example.qrscore.fragment.MapFragment;
import com.example.qrscore.fragment.ProfileFragment;
import com.example.qrscore.fragment.ScanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * Purpose: This class is the main activity
 *
 * Outstanding issues:
 */
public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavView;
    HomeFragment homeFragment = new HomeFragment();
    MapFragment mapFragment = new MapFragment();
    ScanFragment scanFragment = new ScanFragment();
    LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    static private Account account;
    final static public String ACCOUNT_KEY = "ACCOUNT";
    String lastViewedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Intent intent = new Intent(MainActivity.this, OtherPlayerAccountActivity.class);
        //intent.putExtra("userID", "008pIplmeCdA35SkXKh2B2fL0B82");
        //startActivity(intent);

        // Authorize User.
        // Initialize HomeFragment when open app.
        bottomNavView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        bottomNavView.setOnItemSelectedListener(new NavBarOnItemSelectedListener());

        if (savedInstanceState != null) {
            lastViewedFragment = savedInstanceState.getString("SAVED_FRAGMENT");

            switch (lastViewedFragment) {
                case "homeFragment":
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
                    break;
                case "mapFragment":
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mapFragment).commit();
                    break;
                case "scanFragment":
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, scanFragment).commit();
                    break;
                case "leaderboardFramgent":
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, leaderboardFragment).commit();
                    break;
                case "profileFragment":
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, profileFragment).commit();
                    break;
            }
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
        }
    }

    // Bottom Nav selector.
    // https://www.youtube.com/watch?v=OV25x3a55pk
    private class NavBarOnItemSelectedListener implements NavigationBarView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_fragment_item:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
                    lastViewedFragment = "homeFragment";
                    return true;
                case R.id.map_fragment_item:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mapFragment).commit();
                    lastViewedFragment = "mapFragment";
                    return true;
                case R.id.scan_fragment_item:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, scanFragment).commit();
                    lastViewedFragment = "scanFragment";
                    return true;
                case R.id.leaderboard_fragment_item:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, leaderboardFragment).commit();
                    lastViewedFragment = "leaderboardFragment";
                    return true;
                case R.id.profile_fragment_item:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, profileFragment).commit();
                    lastViewedFragment = "profileFragment";
                    return true;
            }
            return false;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("SAVED_FRAGMENT", lastViewedFragment);

        super.onSaveInstanceState(savedInstanceState);
    }
}

