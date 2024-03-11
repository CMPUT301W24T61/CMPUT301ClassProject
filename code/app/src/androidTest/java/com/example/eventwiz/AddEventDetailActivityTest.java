package com.example.eventwiz;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
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
 * Test Class that aims to test the AddEventDetailActivity
 * @See: AddEventDetailActivity
 */
public class AddEventDetailActivityTest {

    @Rule
    public ActivityScenarioRule<AddEventDetailActivity> scenario = new ActivityScenarioRule<>(AddEventDetailActivity.class);

    @Test
    /**
     * Test function that aims to test the addition of Event details by the user and moving to the
     * next step
     */
    public void TestFillAndNext() {
        //Add Event Name
        onView(withId(R.id.etEventName)).perform(ViewActions.typeText("New Event"));
        onView(withId(R.id.etEventName)).perform(ViewActions.closeSoftKeyboard());
        //Set the day
        onView(withId(R.id.spinnerDay)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("30"))).perform(click());
        onView(withId(R.id.spinnerDay)).check(matches(withSpinnerText(containsString("30"))));
        //Set the month
        onView(withId(R.id.spinnerMonth)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("April"))).perform(click());
        onView(withId(R.id.spinnerMonth)).check(matches(withSpinnerText(containsString("April"))));
        //Set the Year
        onView(withId(R.id.spinnerYear)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2024"))).perform(click());
        onView(withId(R.id.spinnerYear)).check(matches(withSpinnerText(containsString("2024"))));
        //Set from hour
        onView(withId(R.id.spinnerFromHour)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("06"))).perform(click());
        onView(withId(R.id.spinnerFromHour)).check(matches(withSpinnerText(containsString("06"))));
        //Set from minute
        onView(withId(R.id.spinnerFromMinute)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("30"))).perform(click());
        onView(withId(R.id.spinnerFromMinute)).check(matches(withSpinnerText(containsString("30"))));
        //Set from AM/PM
        onView(withId(R.id.fromAmPM)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("PM"))).perform(click());
        onView(withId(R.id.fromAmPM)).check(matches(withSpinnerText(containsString("PM"))));
        //Set to hour
        onView(withId(R.id.spinnerToHour)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("08"))).perform(click());
        onView(withId(R.id.spinnerToHour)).check(matches(withSpinnerText(containsString("08"))));
        //Set To Minute
        onView(withId(R.id.spinnerToMinute)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("45"))).perform(click());
        onView(withId(R.id.spinnerToMinute)).check(matches(withSpinnerText(containsString("45"))));
        //Set to AM/PM
        onView(withId(R.id.toAmPM)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("PM"))).perform(click());
        onView(withId(R.id.toAmPM)).check(matches(withSpinnerText(containsString("PM"))));
        //Set description
        onView(withId(R.id.etEventDescription)).perform(ViewActions.typeText("Test Description"));
        onView(withId(R.id.etEventDescription)).perform(ViewActions.closeSoftKeyboard());
        //Set the total attendees
        onView(withId(R.id.etMaxAttendees)).perform(ViewActions.typeText("10"));
        onView(withId(R.id.etMaxAttendees)).perform(ViewActions.closeSoftKeyboard());
        //Click the Next button
        onView(withId(R.id.btnNext)).perform(click());
        //Check elements to see if the next window has opened
        onView(withId(R.id.tvAddressLine1)).check(matches(isDisplayed()));
        onView(withId(R.id.tvAddressLine2)).check(matches(isDisplayed()));
        onView(withId(R.id.tvAddressLine3)).check(matches(isDisplayed()));
    }
}
