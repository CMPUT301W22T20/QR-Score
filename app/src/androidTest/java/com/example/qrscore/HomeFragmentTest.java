package com.example.qrscore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrscore.activity.MainActivity;
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
    public void scanFragmentAndConfirmUpdatedHome() {
        View view = solo.getView(R.id.scan_fragment_item);
        solo.clickOnView(view);
        assertTrue(solo.searchButton("Scan"));
        solo.clickOnButton("Scan");
        solo.waitForText("Confirm");
        solo.clickOnButton("Confirm");
        view = solo.getView(R.id.home_fragment_item);
        solo.clickOnView(view);
        solo.sleep(2000);
        assertTrue(solo.getCurrentActivity() instanceof MainActivity);
        TextView myScanned = solo.getView(TextView.class, 2);
        TextView myQRScore = solo.getView(TextView.class, 4);
        TextView myRank = solo.getView(TextView.class, 6);
        String myScannedString = (String) myScanned.getText();
        String myQRScoreString = (String) myQRScore.getText();
        String myRankString = (String) myRank.getText();
        assertEquals(myScanned.getHint(), "(# scanned)");
        assertEquals(myQRScore.getHint(), "(score #)");
        assertEquals(myRank.getHint(), "(rank #)");
        assertEquals(myScannedString, "1");
        assertEquals(myQRScoreString, "0");
        assertEquals(myRankString, "NIL");
    }
}
