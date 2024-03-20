package com.example.eventwiz;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.checkerframework.checker.units.qual.A;
import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class AdminDashboardTest {
    @Rule
    public ActivityScenarioRule<AdminDashboard> scenario = new ActivityScenarioRule<>(AdminDashboard.class);
    @Rule
    public IntentsTestRule<AdminDashboard> intentsTestRule = new IntentsTestRule<>(AdminDashboard.class);

    @Test
    /**
     * This test function checks the events button on the admin dashboard
     *
     */
    public void TestAdminEventsButton() {
        //Click on the browse Events Button
        onView(withId(R.id.events)).perform(click());
        Intents.intended(hasComponent(BrowseEventsActivity.class.getName()));

    }
    @Test
    /**
     * This test function checks the users button on the admin dashboard
     *
     */
    public void TestAdminUsersButton() {
        //Click on the browse Events Button
        onView(withId(R.id.users)).perform(click());
        Intents.intended(hasComponent(AdminBrowseUsersActivity.class.getName()));

    }
    @Test
    /**
     * This test function checks the images button on the admin dashboard
     *
     */
    public void TestAdminImagesButton() {
        //Click on the browse Events Button
        onView(withId(R.id.images)).perform(click());
        Intents.intended(hasComponent(AdminBrowseImages.class.getName()));

    }
}
