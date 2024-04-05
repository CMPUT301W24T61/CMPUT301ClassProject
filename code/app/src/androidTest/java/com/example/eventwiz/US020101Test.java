package com.example.eventwiz;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class US020101Test {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testCheckInWithQRCode(){

    }

    private void create_profile() {
        //Click create profile button
        onView(withId(R.id.button_register)).perform(click());
        //Fill the name
        onView(withId(R.id.editText_register_name)).perform(ViewActions.typeText("Test Name"));
        onView(withId(R.id.editText_register_name)).perform(ViewActions.closeSoftKeyboard());
        //Fill in the email
        onView(withId(R.id.editText_register_email)).perform(ViewActions.typeText("TestEmail@mail.com"));
        onView(withId(R.id.editText_register_email)).perform(ViewActions.closeSoftKeyboard());
        //Fill in the homepage
        onView(withId(R.id.editText_homepage)).perform(ViewActions.typeText("TestHmpg.com"));
        onView(withId(R.id.editText_homepage)).perform(ViewActions.closeSoftKeyboard());
        //Fill in the number
        onView(withId(R.id.editText_mobile)).perform(ViewActions.typeText("000-000-0000"));
        onView(withId(R.id.editText_mobile)).perform(ViewActions.closeSoftKeyboard());
        //Click on the done button
        onView(withId(R.id.button_register)).check(matches(withText("Done")));
        onView(withId(R.id.button_register)).perform(click());
        //click on the positive button on the pop up
        onView(withText("OK")).perform(click());
    }
}
