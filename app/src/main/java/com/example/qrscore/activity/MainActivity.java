package com.example.qrscore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.os.Build;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.MenuItem;
import com.example.qrscore.Account;
import com.example.qrscore.R;
import com.example.qrscore.fragment.HomeFragment;
import com.example.qrscore.fragment.LeaderboardFragment;
import com.example.qrscore.fragment.MapFragment;
import com.example.qrscore.fragment.ProfileFragment;
import com.example.qrscore.fragment.ScanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

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
    FloatingActionButton scanFragmentAdd;
    FloatingActionButton scanFragmentView;
    Animation fabOpen;
    Animation fabClose;
    Boolean isfabOpen;

    static private Account account;
    final static public String ACCOUNT_KEY = "ACCOUNT";
    String lastViewedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

        //Intent intent = new Intent(MainActivity.this, OtherPlayerAccountActivity.class);
        //intent.putExtra("userID", "008pIplmeCdA35SkXKh2B2fL0B82");
        //startActivity(intent);

        // Authorize User.
        // Initialize HomeFragment when open app.
        bottomNavView = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        bottomNavView.setOnItemSelectedListener(new NavBarOnItemSelectedListener());
        isfabOpen = false;
        scanFragmentAdd = (FloatingActionButton) findViewById(R.id.scan_fragment_add_qr_fab);
        scanFragmentView = (FloatingActionButton) findViewById(R.id.scan_fragment_view_profile_fab);
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.scan_fragment_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.scan_fragment_close);

//        if (savedInstanceState != null) {
//            lastViewedFragment = savedInstanceState.getString("SAVED_FRAGMENT");
//
//            switch (lastViewedFragment) {
//                case "homeFragment":
//                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
//                    break;
//                case "mapFragment":
//                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mapFragment).commit();
//                    break;
//                case "scanFragment":
//                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, scanFragment).commit();
//                    break;
//                case "leaderboardFramgent":
//                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, leaderboardFragment).commit();
//                    break;
//                case "profileFragment":
//                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, profileFragment).commit();
//                    break;
//            }
//        }
//        else {
//            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
//        }
    }

    // Bottom Nav selector.
    // https://www.youtube.com/watch?v=OV25x3a55pk
    private class NavBarOnItemSelectedListener implements NavigationBarView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_fragment_item:
                    if (isfabOpen) {
                        closeScanFab();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
                    lastViewedFragment = "homeFragment";
                    return true;
                case R.id.map_fragment_item:
                    if (isfabOpen) {
                        closeScanFab();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mapFragment).commit();
                    lastViewedFragment = "mapFragment";
                    return true;
                case R.id.scan_fragment_item:
                    animationScanFab();
                    scanFragmentAdd.setOnClickListener(view -> {
                        closeScanFab();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, scanFragment).commit();
                    });
                    scanFragmentView.setOnClickListener(view -> {
                        closeScanFab();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, scanFragment).commit();
                    });
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, scanFragment).commit();
                    lastViewedFragment = "scanFragment";
                    return true;
                case R.id.leaderboard_fragment_item:
                    if (isfabOpen) {
                        closeScanFab();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, leaderboardFragment).commit();
                    lastViewedFragment = "leaderboardFragment";
                    return true;
                case R.id.profile_fragment_item:
                    if (isfabOpen) {
                        closeScanFab();
                    }
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

    // https://www.youtube.com/watch?v=HGQ-8pjI7HM
    private void animationScanFab() {
        if (isfabOpen) {
            closeScanFab();
        }
        else {
            openScanFab();
        }
    }

    private void openScanFab() {
        scanFragmentAdd.startAnimation(fabOpen);
        scanFragmentView.startAnimation(fabOpen);
        scanFragmentAdd.setClickable(true);
        scanFragmentView.setClickable(true);
        isfabOpen = true;
    }

    private void closeScanFab() {
        scanFragmentAdd.startAnimation(fabClose);
        scanFragmentView.startAnimation(fabClose);
        scanFragmentAdd.setClickable(false);
        scanFragmentView.setClickable(false);
        isfabOpen = false;
    }

    public void hideFab(View view){
        if (isfabOpen) {
            closeScanFab();
        }
    }

    // https://www.youtube.com/watch?v=N8mDDevOj-I
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

}

