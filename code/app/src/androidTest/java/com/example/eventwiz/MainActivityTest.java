package com.example.eventwiz;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
/**
* This is a test class to test the UI component of MainActivity
* <p>
 *     NOTE: button_scan_qr is not tested as it involves the use of external APIs and other apps
 *     @See: MainActivity.java
* */
public class MainActivityTest {

    /**
     * Rule that will always execute before the start of every test
     */
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Test Function to check the functionality of the "Create Profile" button
     */
    @Test
    public void TestCreateProfileButton() {
        //Click on the create profile button
        onView(withId(R.id.create_profile_btn)).perform(click());
        //Check that the create profile header is present
        //This confirms that we moved to the right page
        onView(withId(R.id.textView_register_head)).check(matches(isDisplayed()));

    }

    /**
     * Test Function to check the functionality of "Get Started" Button
     */
    @Test
    public void TestGetStartedButton() {
        //Click on the Get Started Button
        onView(withId(R.id.button_get_started)).perform(click());
        //Check for Welcome header
        onView(withId(R.id.Welcome)).check(matches(isDisplayed()));
    }
}
