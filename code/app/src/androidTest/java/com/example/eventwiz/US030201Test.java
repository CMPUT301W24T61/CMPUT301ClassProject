package com.example.eventwiz;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.eventwiz.UITestMatchers.withDrawable;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.not;

import static java.lang.Thread.sleep;


import android.app.Activity;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
/*
 * This UI Component Test helps test the User Story 3.2.1
 * Requirements: As an User, I want to login to toggle my geo location
 *
 */
public class US030201Test {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void TestUserStory() throws InterruptedException {
        //Click on geolocation button
        click_geolocation_button();
        //Check off
        check_off();
        //click on geolocation
        click_geolocation_button();
        //Check off
        check_off();
        //Click on geolocation
        click_geolocation_button();
        //check on
        check_on();
        //click on geolocation
        click_geolocation_button();
        //check on
        check_on();
    }

    private void click_geolocation_button() throws InterruptedException {
        //Click on geolocation button
        onView(withId(R.id.button_geolocation)).perform(click());
        //Click on the notification yes
        onView(withText("Yes")).perform(click());
        Thread.sleep(1500);
    }
    private void check_on() {
        onView(withId(R.id.gps_status)).check(matches(withText("Location ON")));
    }

    private void check_off() {
        onView(withId(R.id.gps_status)).check(matches(withText("Location OFF")));
    }

}
