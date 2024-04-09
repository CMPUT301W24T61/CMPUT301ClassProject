package com.example.eventwiz;
<<<<<<< HEAD
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
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

=======

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static java.lang.Thread.sleep;

>>>>>>> main
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
<<<<<<< HEAD
/*
 * This UI Component Test helps test the User Story 2.2.3
 * Requirements: As an attendee, I want to update information such as name, hompage, and email
 */
=======

>>>>>>> main
public class US020203Test {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Test
<<<<<<< HEAD
    public void TestUserStory() throws InterruptedException {
        //create a profile to proceed
        create_profile();
        //Go to the user dashboard
        go_to_user_dashboard();
        go_to_my_profile();
        click_on_edit_profile_button();
        //Update the profile
        update_profile();
        //Go to dashboard
        go_to_user_dashboard();
        //Go to my profile
        go_to_my_profile();
        Thread.sleep(2000);

    }

    private void create_profile() throws InterruptedException {
=======
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
>>>>>>> main
        //Click create profile button
        onView(withId(R.id.button_register)).perform(click());
        //Fill the name
        onView(withId(R.id.editText_register_name)).perform(ViewActions.typeText("Test Name"));
        onView(withId(R.id.editText_register_name)).perform(ViewActions.closeSoftKeyboard());
        //Fill in the email
<<<<<<< HEAD
        onView(withId(R.id.editText_register_email)).perform(ViewActions.typeText("TestEmail@gmail.com"));
=======
        onView(withId(R.id.editText_register_email)).perform(ViewActions.typeText("TestEmail@mail.com"));
>>>>>>> main
        onView(withId(R.id.editText_register_email)).perform(ViewActions.closeSoftKeyboard());
        //Fill in the homepage
        onView(withId(R.id.editText_homepage)).perform(ViewActions.typeText("TestHmpg.com"));
        onView(withId(R.id.editText_homepage)).perform(ViewActions.closeSoftKeyboard());
        //Fill in the number
        onView(withId(R.id.editText_mobile)).perform(ViewActions.typeText("000-000-0000"));
        onView(withId(R.id.editText_mobile)).perform(ViewActions.closeSoftKeyboard());
<<<<<<< HEAD
        //Select a profile picture
        onView(withId(R.id.profile_pic_button)).perform(click());
        Thread.sleep(5000);
        //Click on the done button
        onView(withId(R.id.button_register)).check(matches(withText("Done")));
        onView(withId(R.id.button_register)).perform(click());
        Thread.sleep(3500);
    }

    private void go_to_user_dashboard() throws InterruptedException {
        //Click on the get started button
        onView(withId(R.id.button_get_started)).perform(click());
        //Check if user dashboard is reached
        onView(withId(R.id.createEvent)).check(matches(isDisplayed()));
        onView(withId(R.id.myHostedEvents)).check(matches(isDisplayed()));
        Thread.sleep(3500);

    }


    private void go_to_my_profile() {
        //Click on the my profile button
        onView(withId(R.id.myProfile)).perform(click());
        //Check that my profile page has been reached
        onView(withId(R.id.text_name)).check(matches(isDisplayed()));
        onView(withId(R.id.text_email)).check(matches(isDisplayed()));
    }

    private void click_on_edit_profile_button() {
        //Click on the edit profile button
        onView(withId(R.id.edit_profile)).perform((click()));
    }

    private void update_profile() throws InterruptedException {
        //Change the name
        onView(withId(R.id.editText_register_name)).perform(clearText(), typeText("New name"), closeSoftKeyboard());
        //Change the email
        onView(withId(R.id.editText_register_email)).perform(clearText(), typeText("newmail@gmail.com"), closeSoftKeyboard());
        //Change the homepage
        onView(withId(R.id.editText_homepage)).perform(clearText(), typeText("newhmpg.com"), closeSoftKeyboard());
        //Select a profile picture
        onView(withId(R.id.profile_pic_button)).perform(click());
        Thread.sleep(5000);
        //Click on the done button
        onView(withId(R.id.button_register)).check(matches(withText("Done")));
        onView(withId(R.id.button_register)).perform(click());
        Thread.sleep(500);
=======
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
>>>>>>> main
    }
}
