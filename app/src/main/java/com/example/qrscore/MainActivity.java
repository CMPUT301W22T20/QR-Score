package com.example.qrscore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavView;
    HomeFragment homeFragment = new HomeFragment();
    MapFragment mapFragment = new MapFragment();
    ScanFragment scanFragment = new ScanFragment();
    LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    static private Account account;
    final static public String ACCOUNT_KEY = "ACCOUNT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Authorize User.
        // Initialize HomeFragment when open app.
        bottomNavView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
        bottomNavView.setOnItemSelectedListener(new NavBarOnItemSelectedListener());
//        account = new Account();
    }

    // Bottom Nav selector.
    // https://www.youtube.com/watch?v=OV25x3a55pk
    private class NavBarOnItemSelectedListener implements NavigationBarView.OnItemSelectedListener {
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
    }
}