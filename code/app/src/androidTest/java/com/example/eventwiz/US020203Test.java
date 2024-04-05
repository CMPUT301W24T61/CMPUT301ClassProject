package com.example.eventwiz;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static java.lang.Thread.sleep;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class US020203Test {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testUpdateProfile() throws InterruptedException {
        //create a profile to proceed
        create_profile();
        //Go to the user dashboard
        goto_userDashboard();
        sleep(250);
        //Check_userdashboard();
        check_userDashboard();

        //Go to user Profile
        goto_userProfile();
        //Check user Profile update
        checkPrifileUpdate();
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

    private void goto_userDashboard() {
        //Click on get started
        onView(withId(R.id.button_get_started)).perform(click());
    }

    private void check_userDashboard() {
        //Check create event button there
        onView(withId(R.id.createEvent)).check(matches(isDisplayed()));
        onView(withId(R.id.createEvent)).check(matches(withText("Create Event")));
        //Check hosted events
        onView(withId(R.id.myHostedEvents)).check(matches(isDisplayed()));
        onView(withId(R.id.myHostedEvents)).check(matches(withText("My Hosted Events")));

    }

    private void goto_userProfile() {
        onView(withId(R.id.myProfile)).perform(click());
        onView(withId(R.id.edit_profile)).perform(click());
        //Fill the name
        onView(withId(R.id.editText_register_name)).perform(ViewActions.typeText("Colton Connick"));
        onView(withId(R.id.editText_register_name)).perform(ViewActions.closeSoftKeyboard());
        //Fill in the email
        onView(withId(R.id.editText_register_email)).perform(ViewActions.typeText("connick@ualberta.ca"));
        onView(withId(R.id.editText_register_email)).perform(ViewActions.closeSoftKeyboard());
        //Fill in the homepage
        onView(withId(R.id.editText_homepage)).perform(ViewActions.typeText("Smile.com"));
        onView(withId(R.id.editText_homepage)).perform(ViewActions.closeSoftKeyboard());
        //Fill in the number
        onView(withId(R.id.editText_mobile)).perform(ViewActions.typeText("555-555-5555"));
        onView(withId(R.id.editText_mobile)).perform(ViewActions.closeSoftKeyboard());
        //Click on the done button
        onView(withId(R.id.button_register)).check(matches(withText("Done")));
        onView(withId(R.id.button_register)).perform(click());
        //click on the positive button on the pop up
        onView(withText("OK")).perform(click());
    }

    private void checkPrifileUpdate() {
        //Go back to user Dashboard
        onView(withId(R.id.button_get_started)).perform(click());
        //Go to My Profile
        onView(withId(R.id.myProfile)).perform(click());

        onView(withId(R.id.text_name)).check(matches(isDisplayed()));
    }
}
