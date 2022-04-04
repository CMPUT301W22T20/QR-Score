package com.example.qrscore;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrscore.activity.MainActivity;
import com.example.qrscore.activity.QRCodeActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HomeFragmentTest {
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
    public void gotoHomeFragment(){
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.home_fragment_item));

        assertTrue(solo.searchText("Home"));
    }

    @Test
    public void viewOtherPlayer() throws InterruptedException {
        View view = solo.getView(R.id.home_fragment_item);

        solo.clickOnView(view);
        assertTrue(solo.searchText("Home"));
        solo.clickOnImageButton(0);
        solo.waitForText("View QR Code");
        solo.clickOnText("View QR Code");
        assertTrue(solo.getCurrentActivity() instanceof QRCodeActivity);

    }
}
