package com.example.qrscore;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class QRCodeActivityTest {
    private Solo solo;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    @Rule
    public ActivityTestRule<QRCodeActivity> rule =
            new ActivityTestRule<>(QRCodeActivity.class, true, true);

    /**
     * Runs before all tests and creates a solo instance and initializes the db.
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Comment");
    }

    /**
     * Gets the activity.
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Add a comment and check if it shows up with assertEquals
     * Click on the comment and check if the comment is the same with assertEquals
     */
    @Test
    public void checklist() {
        solo.assertCurrentActivity("Wrong activity", QRCodeActivity.class);

        solo.clickOnButton("Add");

        solo.enterText((EditText) solo.getView(R.id.comment_editText), "Test Comment");

        solo.clickOnButton("Done");

        assertTrue(solo.waitForText("Player1", 1, 2000));

        solo.clickOnText("Player1");

        assertTrue(solo.waitForText("Test Comment", 1, 2000));
    }

    /**
     * Checks if our list contains the same value as what is shown
     */
    @Test
    public void checkCommentList() {
        solo.assertCurrentActivity("Wrong activity", QRCodeActivity.class);

        solo.waitForText("Player1", 1, 2000);

        QRCodeActivity activity = (QRCodeActivity) solo.getCurrentActivity();

        final ListView commentList = activity.getCommentList();

        Comment comment = (Comment) commentList.getItemAtPosition(0);

        assertEquals("Test Comment", comment.getComment());
    }

    @After
    public void teardown() throws Exception {
        solo.finishOpenedActivities();
    }
}
