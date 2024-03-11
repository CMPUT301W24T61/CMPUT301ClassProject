package com.example.eventwiz;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
/**
 * Test Class that aims to test the AddEventLocationActivity
 *
 * @See: AddEventLocationActivity
 */
public class AddEventLocationActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    /**
     * Test function that aims to test the UI component of filling out of the Event Location during
     * event creation process
     */
    public void TestFillAndNext() {
        //Click on get Started button
        onView(withId(R.id.button_get_started)).perform(click());
        //Click on Create Event
        onView(withId(R.id.createEvent)).perform(click());
        //Click on Next Button
        onView(withId(R.id.btnNext)).perform(click());
        //Type Address LIne 1
        onView(withId(R.id.etAddressLine1)).perform(ViewActions.typeText("Test Addr Line 1"));
        onView(withId(R.id.etAddressLine1)).perform(ViewActions.closeSoftKeyboard());
        //Type Address Line 2
        onView(withId(R.id.etAddressLine2)).perform(ViewActions.typeText("Test Addr Line 2"));
        onView(withId(R.id.etAddressLine2)).perform(ViewActions.closeSoftKeyboard());
        //Type Address Line 3
        onView(withId(R.id.etAddressLine3)).perform(ViewActions.typeText("Test Addr Line 3"));
        onView(withId(R.id.etAddressLine3)).perform(ViewActions.closeSoftKeyboard());
        //Type City
        onView(withId(R.id.etCity)).perform(ViewActions.typeText("Edmonton"));
        onView(withId(R.id.etCity)).perform(ViewActions.closeSoftKeyboard());
        //Type Province
        onView(withId(R.id.etStateProvince)).perform(ViewActions.typeText("Alberta"));
        onView(withId(R.id.etStateProvince)).perform(ViewActions.closeSoftKeyboard());
        //Type Country
        onView(withId(R.id.etCountry)).perform(ViewActions.typeText("Canada"));
        onView(withId(R.id.etCountry)).perform(ViewActions.closeSoftKeyboard());
        //Type Area Code
        onView(withId(R.id.etAreaCodePostalCode)).perform(ViewActions.typeText("T6G XXX"));
        onView(withId(R.id.etAreaCodePostalCode)).perform(ViewActions.closeSoftKeyboard());
        //Click on the next button
        onView(withId(R.id.btnNext)).perform(click());
        //Check if the next window opened
        onView(withId(R.id.uploadHeader)).check(matches(isDisplayed()));
    }
}
