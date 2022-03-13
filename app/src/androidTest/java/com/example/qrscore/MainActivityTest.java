package com.example.qrscore;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentManager;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUP() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testLogin() {
        solo.waitForText("Login Successful!", 1, 2000);
    }

    @Test
    public void testProfileFragment() {
        ProfileController profileController = new ProfileController(ApplicationProvider.getApplicationContext());
        // Goto profile
        solo.clickOnView(solo.getView(R.id.profile_fragment_item));
        // Edit profile
        solo.clearEditText((EditText) solo.getView(R.id.first_name_textInputEditText));
        solo.enterText((EditText) solo.getView(R.id.first_name_textInputEditText), "Test");
        solo.clearEditText((EditText) solo.getView(R.id.last_name_textInputEditText));
        solo.enterText((EditText) solo.getView(R.id.last_name_textInputEditText), "Case");
        solo.clearEditText((EditText) solo.getView(R.id.email_textInputEditText));
        solo.enterText((EditText) solo.getView(R.id.email_textInputEditText), "testcase@qrscore.io");
        solo.clearEditText((EditText) solo.getView(R.id.phone_number_textInputEditText));
        solo.enterText((EditText) solo.getView(R.id.phone_number_textInputEditText), "7803012022");
        // Save profile
        solo.clickOnView(solo.getView(R.id.profile_save_button));
        solo.waitForText("Profile has been updated!", 1, 2000);
        // Click on other fragment and check that profile has not changed.
        solo.clickOnView(solo.getView(R.id.leaderboard_fragment_item));
        solo.clickOnView(solo.getView(R.id.profile_fragment_item));
        solo.searchText("Test");
        solo.searchText("Case");
        solo.searchText("testcase@qrscore.io");
        solo.searchText("7803012022");
        solo.searchText(profileController.getProfile().getUserUID());    // Checks for UserUID/username
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
