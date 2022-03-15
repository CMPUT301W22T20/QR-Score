package com.example.qrscore;

import android.app.Activity;
import android.app.Fragment;
import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import junit.framework.TestCase.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ScanFragmentTest {
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
    public void gotoScanFragment(){
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.scan_fragment_item));

        assertTrue(solo.searchButton("Scan"));
    }
}
