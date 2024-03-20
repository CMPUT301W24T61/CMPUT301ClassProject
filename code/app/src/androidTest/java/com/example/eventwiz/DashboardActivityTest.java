package com.example.eventwiz;
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

@RunWith(AndroidJUnit4.class)
@LargeTest
/**
 * Test Class that aims to test the functioning of DashboardActivity
 *
 * @See: DashboardActivity, BrowseEventsActivity, CreateEventActivity
 */
public class DashboardActivityTest {
    @Rule
    public ActivityScenarioRule<DashboardActivity> scenario = new ActivityScenarioRule<>(DashboardActivity.class);@Rule
    public IntentsTestRule<DashboardActivity> intentsTestRule = new IntentsTestRule<>(DashboardActivity.class);

    @Test
    /**
     * Test function that aims to test the navigation to the create event page
     */
    public void TestCreateEventButton() {
        //Click on the Create Event Button
        onView(withId(R.id.createEvent)).perform(click());
        //Check for presence of the Event Details page
        onView(withId(R.id.tvEventNameLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.tvSetDateLabel)).check(matches(isDisplayed()));
        Intents.intended(hasComponent(AddEventDetailActivity.class.getName()));
//        onView(withId(R.id.btnNext1)).check(matches(isDisplayed()));
    }

    @Test
    /**
     * Test function that aims to test the navigation to the browse events page
     */
    public void TestBrowseEventsButton() {
        //Click on the Browse Events Button
        onView(withId(R.id.browseEvents)).perform(click());
        //Check for Views present in the browse events page
        //We are going to pick the ListView
        onView(withId(R.id.lvEvents)).check(matches(isDisplayed()));
        Intents.intended(hasComponent(BrowseEventsActivity.class.getName()));
    }

}
