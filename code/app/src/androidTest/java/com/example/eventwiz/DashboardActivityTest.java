/*Testing still in progress

package com.example.eventwiz;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.eventwiz.DashboardActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class DashboardActivityTest {

    @Rule
    public ActivityScenarioRule<DashboardActivity> activityRule =
            new ActivityScenarioRule<>(DashboardActivity.class);

    @Test
    public void testCreateEventButton() {
        // Click on the createEventButton
        Espresso.onView(ViewMatchers.withId(R.id.createEvent)).perform(ViewActions.click());

        // Check if the AddEventDetailActivity is launched
        Espresso.onView(ViewMatchers.withId(R.id.addEventDetailLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testProfileButton() {
        // Click on the profileButton
        Espresso.onView(ViewMatchers.withId(R.id.myProfile)).perform(ViewActions.click());

        // Check if the ViewProfileActivity is launched
        Espresso.onView(ViewMatchers.withId(R.id.view_profile))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testScanQRButton() {
        // Click on the scanQRButton
        Espresso.onView(ViewMatchers.withId(R.id.fabCamera)).perform(ViewActions.click());

        // Check if the QRCodeScannerActivity is launched
        Espresso.onView(ViewMatchers.withId(R.id.activity_qrcode_scanner))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    // Add more tests for other UI components as needed

}
*/