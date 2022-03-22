package com.example.qrscore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.media.Image;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrscore.activity.MainActivity;
import com.example.qrscore.activity.OtherPlayerAccountActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LeaderboardFragmentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void gotoLeaderboardFragment(){
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.leaderboard_fragment_item));

        assertTrue(solo.searchText("QR CODE"));
        assertTrue(solo.searchText("Player"));
    }

    @Test
    public void viewOtherPlayer() throws InterruptedException {
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        View view = solo.getView(R.id.leaderboard_fragment_item);

        solo.clickOnView(view);
        assertTrue(solo.searchText("Player"));
        solo.clickOnText("Player");
        solo.clickOnImageButton(0);
        solo.waitForText("View Player");
        solo.clickOnText("View Player");
        assertTrue(solo.getCurrentActivity() instanceof OtherPlayerAccountActivity);

    }
}
