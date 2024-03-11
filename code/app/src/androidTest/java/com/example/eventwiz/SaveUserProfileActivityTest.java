package com.example.eventwiz;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
/**
 * This test class aims to test the SaveUserProfileActivity UI component
 *
 * Note: Run the app on the emulator freshly and then run the test for it to be successful
 * @See: SaveUserProfileActivity
 */
public class SaveUserProfileActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);
    @Test
    /**
     * Test function that tests the UI component for the creation of a Profile
     */
    public void TestProfileCreation() {
        //Click on the Create Profile button

        onView(withId(R.id.button_register)).perform(click());
        //Fill the name field
        onView(withId(R.id.editText_register_name)).perform(ViewActions.typeText("Chinmoy"));
        onView(withId(R.id.editText_register_name)).perform(ViewActions.closeSoftKeyboard());
        //Fill the email field
        onView(withId(R.id.editText_register_email)).perform(ViewActions.typeText("c@uab.ca"));
        onView(withId(R.id.editText_register_email)).perform(ViewActions.closeSoftKeyboard());
        //File the Homepage Field
        onView(withId(R.id.editText_homepage)).perform(ViewActions.typeText("testpage.com"));
        onView(withId(R.id.editText_homepage)).perform(ViewActions.closeSoftKeyboard());
        //Fill the mobile number
        onView(withId(R.id.editText_mobile)).perform(ViewActions.typeText("0500000000"));
        onView(withId(R.id.editText_mobile)).perform(ViewActions.closeSoftKeyboard());
        //Click save profile
        onView(withId(R.id.button_register)).perform(click());
        Intents.intended(hasComponent(MainActivity.class.getName()));
    }
}