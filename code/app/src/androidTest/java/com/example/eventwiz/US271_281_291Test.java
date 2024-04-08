package com.example.eventwiz;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.not;

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
/*
 * This UI Component Test helps test the User Stories 2.7.1,  2.9.1
 * Requirement: As an attendee, I want to sign up to attend an event from the event details (as in I promise to go).
 * Requirements: As an attendee, I want to know what events I signed up for currently and in the future.
 *
 */
public class US271_281_291Test {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void TestUserStory() throws InterruptedException {
        //create a profile to proceed
        create_profile();
        //Go to the user dashboard
        go_to_user_dashboard();
        //go to browse events
        go_to_browse_events();
        //click first event
        click_first_event();
        //sign up and back
        click_on_signup_and_back();
        //Click on second event
        click_second_event();
        //sign up and back
        click_on_signup_and_back();
        //Back to dashboard
        back_to_dashboard();
        //go to signed up events
        go_to_signedup_events();
        //Check the events
        check_signup();


    }

    private void create_profile() throws InterruptedException {
        //Click create profile button
        onView(withId(R.id.button_register)).perform(click());
        //Fill the name
        onView(withId(R.id.editText_register_name)).perform(ViewActions.typeText("Test Name"));
        onView(withId(R.id.editText_register_name)).perform(ViewActions.closeSoftKeyboard());
        //Fill in the email
        onView(withId(R.id.editText_register_email)).perform(ViewActions.typeText("TestEmail@gmail.com"));
        onView(withId(R.id.editText_register_email)).perform(ViewActions.closeSoftKeyboard());
        //Fill in the homepage
        onView(withId(R.id.editText_homepage)).perform(ViewActions.typeText("TestHmpg.com"));
        onView(withId(R.id.editText_homepage)).perform(ViewActions.closeSoftKeyboard());
        //Fill in the number
        onView(withId(R.id.editText_mobile)).perform(ViewActions.typeText("000-000-0000"));
        onView(withId(R.id.editText_mobile)).perform(ViewActions.closeSoftKeyboard());
        //Select a profile picture
        onView(withId(R.id.profile_pic_button)).perform(click());
        Thread.sleep(3000);
        //Click on the done button
        onView(withId(R.id.button_register)).check(matches(withText("Done")));
        onView(withId(R.id.button_register)).perform(click());
    }

    private void go_to_user_dashboard() throws InterruptedException {
        //Click on the get started button
        onView(withId(R.id.button_get_started)).perform(click());
        //Check if user dashboard is reached
        onView(withId(R.id.createEvent)).check(matches(isDisplayed()));
        onView(withId(R.id.myHostedEvents)).check(matches(isDisplayed()));
        Thread.sleep(1000);

    }

    private void go_to_browse_events() {
        //Go to browse events
        onView(withId(R.id.browseEvents)).perform(click());
    }

    private void click_first_event() {
        //click on the first event
        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(0).perform(click());

    }

    private void click_second_event() {
        //click on the first event
        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(1).perform(click());

    }

    private void click_on_signup_and_back() throws InterruptedException {
        onView(withId(R.id.ScrlVw)).perform(swipeUp());
        Thread.sleep(500);
        //Click on the sign up button
        onView(withId(R.id.btnSignUp)).perform(click());
        //Check if the messsage displayed
        onData(withText("Signed up successfully")).check(matches(isDisplayed()));
        onView(withId(R.id.ScrlVw)).perform(swipeDown());
        onView(withId(R.id.ScrlVw)).perform(swipeDown());
        Thread.sleep(500);
        //Click on back button
        onView(withId(R.id.goback)).perform(click());
    }

    private void back_to_dashboard() {
        //Click on back button
        onView(withId(R.id.back_arrow)).perform(click());
    }
    private void go_to_signedup_events() {
        //Click on signed up events
        onView(withId(R.id.mySignedUpEvents)).perform(click());
    }

    private void check_signup() {
        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(0).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(1).check(matches(isDisplayed()));
    }
}
