package com.example.qrscore;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrscore.activity.MainActivity;
import com.example.qrscore.activity.OtherPlayerAccountActivity;
import com.example.qrscore.controller.ProfileController;
import com.example.qrscore.fragment.QRGeneratorDialog;
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
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testLogin() {
        // Assumes User is Created.
        solo.clickOnView(solo.getView(R.id.auth_returning_user_button));
        solo.waitForText("Login Successful!", 1, 2000);
    }

    @Test
    public void testHomeFragment() {
        solo.clickOnView(solo.getView(R.id.home_fragment_item));

        solo.clickOnView(solo.getView(R.id.home_fragment_actionbar_qr_code));
        solo.waitForFragmentById(R.id.qr_generator_dialog, 2000);
    }

    @Test
    public void testMapFragment() {
        solo.clickOnView(solo.getView(R.id.map_fragment_item));

        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.map_fragment_item));

        solo.enterText(0, "12");
        solo.enterText(1, "12");
        solo.clickOnButton("Search");
    }

    @Test
    public void testAddQRCodeFragment() {
        solo.clickOnView(solo.getView(R.id.scan_fragment_item));
        solo.clickOnView(solo.getView(R.id.scan_fragment_add_qr_fab));
    }

    @Test
    public void testScanViewProfileFragment() {
        solo.clickOnView(solo.getView(R.id.scan_fragment_item));
        solo.clickOnView(solo.getView(R.id.scan_fragment_view_profile_fab));
    }

    // https://stackoverflow.com/a/33272002
    @Test
    public void testLeaderboardPlayerFragment() {
        ProfileController profileController = new ProfileController(ApplicationProvider.getApplicationContext());

        solo.clickOnView(solo.getView(R.id.leaderboard_fragment_item));

        ViewGroup tabs = (ViewGroup) solo.getView(R.id.leaderboard_tabLayout, 0);
        View leaderboardPlayerTab = ((ViewGroup) tabs.getChildAt(0)).getChildAt(1);
        solo.clickOnView(leaderboardPlayerTab);

        solo.enterText(0, profileController.getProfile().getUserUID());

        solo.clickOnView(solo.getView(R.id.list_item_menu_button));
        solo.clickOnMenuItem("View Player");
        assertTrue(solo.waitForActivity(OtherPlayerAccountActivity.class, 2000));
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
        solo.enterText((EditText) solo.getView(R.id.phone_number_textInputEditText), "(780) 301-2022");
        // Save profile
        solo.clickOnView(solo.getView(R.id.profile_save_button));
        solo.waitForText("Profile has been updated!", 1, 2000);

        // Click on other fragment and check that profile has not changed.
        solo.clickOnView(solo.getView(R.id.leaderboard_fragment_item));
        solo.clickOnView(solo.getView(R.id.profile_fragment_item));

        EditText usernameET = solo.getEditText(profileController.getProfile().getUserUID());
        EditText firstNameET = solo.getEditText("Test");
        EditText lastNameET = solo.getEditText("Case");
        EditText emailET = solo.getEditText("testcase@qrscore.io");
        EditText phoneNumberET = solo.getEditText("301-2022");

        assertEquals("Test", firstNameET.getText().toString());
        assertEquals("Case", lastNameET.getText().toString());
        assertEquals("testcase@qrscore.io", emailET.getText().toString());
        assertEquals("(780) 301-2022", phoneNumberET.getText().toString());
        assertEquals(profileController.getProfile().getUserUID(), usernameET.getText().toString());

        // Check QR Code generator to login other devices
        solo.clickOnView(solo.getView(R.id.profile_generateQR_button));
        solo.waitForFragmentById(R.id.qr_generator_dialog, 2000);
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
