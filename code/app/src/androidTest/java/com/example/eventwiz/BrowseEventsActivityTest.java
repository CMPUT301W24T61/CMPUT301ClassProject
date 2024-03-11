package com.example.eventwiz;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasToString;

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
 * The Test class aims to test the BrowseEventsActivity
 * @See: BrowseEventsActivity
 */
public class    BrowseEventsActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    /**
     * This test function goes through the process of browsing events and clicking on a particular listed event
     *
     */
    public void TestBrowseEvents() {
        //Click on the get started button
        onView(withId(R.id.button_get_started)).perform(click());
        //Click on browse events button
        onView(withId(R.id.browseEvents)).perform(click());
        //Click on the first element in list view
        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(0).perform(ViewActions.click());
        //Check if the event detail page has opened
        onView(withId(R.id.ivCheckInQRCode)).check(matches(isDisplayed()));
        onView(withId(R.id.ivPromotionQRCode)).check(matches(isDisplayed()));
        onView(withId(R.id.ivEventPoster)).check(matches(isDisplayed()));
        Intents.intended(hasComponent(BrowseEventsActivity.class.getName()));

    }
}
