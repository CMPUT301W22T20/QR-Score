package com.example.qrscore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import com.example.qrscore.fragment.ScanFragmentPlayer;
import com.example.qrscore.R;
import com.example.qrscore.fragment.HomeFragment;
import com.example.qrscore.fragment.LeaderboardFragment;
import com.example.qrscore.fragment.MapFragment;
import com.example.qrscore.fragment.ProfileFragment;
import com.example.qrscore.fragment.ScanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

/**
 * Purpose: This class is the main activity which holds the 5 fragments.
 *
 * Outstanding issues:
 */
public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavView;
    HomeFragment homeFragment = new HomeFragment();
    MapFragment mapFragment = new MapFragment();
    ScanFragment scanFragment = new ScanFragment();
    ScanFragmentPlayer scanFragmentSearchPlayer = new ScanFragmentPlayer();
    LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    FloatingActionButton scanFragmentAdd;
    FloatingActionButton scanFragmentView;
    Animation fabOpen;
    Animation fabClose;
    Boolean isfabOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

        bottomNavView = findViewById(R.id.bottom_nav_view);
        bottomNavView.setOnItemSelectedListener(new NavBarOnItemSelectedListener());
        isfabOpen = false;
        scanFragmentAdd = findViewById(R.id.scan_fragment_add_qr_fab);
        scanFragmentView = findViewById(R.id.scan_fragment_view_profile_fab);
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.scan_fragment_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.scan_fragment_close);

        // Initialize HomeFragment when open app.
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
    }

    // Bottom Nav selector.
    // https://www.youtube.com/watch?v=OV25x3a55pk

    /**
     * Purpose: Listener for the bottomNavigationView.
     *
     * Outstanding issues:
     */
    private class NavBarOnItemSelectedListener implements NavigationBarView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_fragment_item:
                    if (isfabOpen) {
                        closeScanFab();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFragment).commit();
                    return true;

                case R.id.map_fragment_item:
                    if (isfabOpen) {
                        closeScanFab();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mapFragment).commit();
                    return true;

                case R.id.scan_fragment_item:
                    animationScanFab();
                    scanFragmentAdd.setOnClickListener(view -> {
                        closeScanFab();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, scanFragment).commit();
                    });
                    scanFragmentView.setOnClickListener(view -> {
                        closeScanFab();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, scanFragmentSearchPlayer).commit();
                    });
                    return true;

                case R.id.leaderboard_fragment_item:
                    if (isfabOpen) {
                        closeScanFab();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, leaderboardFragment).commit();
                    return true;

                case R.id.profile_fragment_item:
                    if (isfabOpen) {
                        closeScanFab();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, profileFragment).commit();
                    return true;

            }
            return false;
        }

    }

    /**
     * Purpose: Function to animate the scan FABs.
     *
     * Learned animations from: https://www.youtube.com/watch?v=HGQ-8pjI7HM
     */
    private void animationScanFab() {
        if (isfabOpen) {
            closeScanFab();
        }
        else {
            openScanFab();
        }
    }

    /**
     * Purpose: Animate function to show scan FABs.
     */
    private void openScanFab() {
        scanFragmentAdd.startAnimation(fabOpen);
        scanFragmentView.startAnimation(fabOpen);
        scanFragmentAdd.setClickable(true);
        scanFragmentView.setClickable(true);
        isfabOpen = true;
    }

    /**
     * Purpose: Animate function to close scan FABs.
     */
    private void closeScanFab() {
        scanFragmentAdd.startAnimation(fabClose);
        scanFragmentView.startAnimation(fabClose);
        scanFragmentAdd.setClickable(false);
        scanFragmentView.setClickable(false);
        isfabOpen = false;
    }

    /**
     * Purpose: Animate function to hide scan FABs when clicked outside FABs.
     */
    public void hideFab(View view){
        if (isfabOpen) {
            closeScanFab();
        }
    }
}

