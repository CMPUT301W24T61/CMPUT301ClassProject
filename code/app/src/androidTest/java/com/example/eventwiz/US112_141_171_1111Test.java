package com.example.eventwiz;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.eventwiz.UITestMatchers.withDrawable;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
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
 * This UI Component Test helps test the User Stories 1.1.2, 1.4.1, and 1.7.1 and 1.11.1
 * Requirement: As an organizer, I want the option to reuse an existing QR Code for attendee check-ins.
 * Requiremment: As an organizer, I want to upload an event poster to provide visual information to attendees.
 * Requirement: As an organizer, I want to create a new event and generate a unique promotion QR code that links to the event description and event poster in the app.
 * As an organizer, I want to OPTIONALLY limit the number of attendees that can sign up for an event.
 */
public class US112_141_171_1111Test {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void TestUserStory() throws InterruptedException {
        //create a profile to proceed
        create_profile();

        //Go to the user dashboard
        goto_userDashboard();
        sleep(250);
        //Check_userdashboard();
        check_userDashboard();
        //Go to the event creation page
        goto_createEvent();
        //Check if event details page reahed
        check_eventDetailsPage();
        //Fill in the event details
        fill_eventDetails();
        //go to the event location detail page
        goto_eventLocationDetails();
        //Check if event location page has been reached
        check_eventLocationDetailsPage();
        //Add the event Location Details
        fill_eventLocationDetails();
        //Go to the net page
        goto_eventAddInfo();
        //check if the page  is reached
        check_eventAddInfo();
        //Fill in the information
        fill_eventAddInfo();
        //Create the event
        click_createEvent();
        //Check the creation success page
        Thread.sleep(4000);
        check_SuccessPage();

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
        onView(withId(R.id.editText_mobile)).perform(ViewActions.typeText("0000000000"));
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
    private void goto_createEvent() {
        //Click on the create event button
        onView(withId(R.id.createEvent)).perform(click());
    }

    private void check_eventDetailsPage() {
        //Check for views to confirm event details page
        //Check for the spinners
        onView(withId(R.id.spinnerToHour)).check(matches(isDisplayed()));
        onView(withId(R.id.spinnerToMinute)).check(matches(isDisplayed()));
        onView(withId(R.id.toAmPM)).check(matches(isDisplayed()));
        onView(withId(R.id.spinnerFromHour)).check(matches(isDisplayed()));
        onView(withId(R.id.spinnerFromMinute)).check(matches(isDisplayed()));
        onView(withId(R.id.fromAmPM)).check(matches(isDisplayed()));

    }

    private void fill_eventDetails() {
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
    }

    private void goto_eventLocationDetails() {
        //Click on the next button
        onView(withId(R.id.btnNext)).perform(click());
    }

    private void check_eventLocationDetailsPage() {
        //Check the address line edit texts to confirm
        onView(withId(R.id.etAddressLine1)).check(matches(isDisplayed()));
        onView(withId(R.id.etAddressLine2)).check(matches(isDisplayed()));
        onView(withId(R.id.etAddressLine3)).check(matches(isDisplayed()));
    }

    private void fill_eventLocationDetails() {
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
    }

    private void goto_eventAddInfo() {
        //Click on the next btn
        onView(withId(R.id.btnNext)).perform(click());
    }

    private void check_eventAddInfo() {
        //We can check the upload header which is concrete uniqueness
        onView(withId(R.id.uploadHeader)).check(matches(withText("Upload an Event Poster (Image File):")));
    }

    private void fill_eventAddInfo() throws InterruptedException {

        //Check the Promotion QR option
        onView(withId(R.id.generatePromotionQRCode)).perform(click());
        //Check the use existing QR code option
        onView(withId(R.id.reuseQRCode)).perform(click());
        //click on the upload button
        onView(withId(R.id.uploadButton)).perform(click());
        //Sleep and wait for 5 seconds
        Thread.sleep(5000);
        //Check if the poster imageview has the poster
        onView(withId(R.id.poster)).check(matches(not(withDrawable(R.drawable.image_placeholder_background))));

    }
    private void click_createEvent() {
        //Click on the create event
        onView(withId(R.id.createEventButton)).perform(click());
    }

    private void check_SuccessPage() {
        //Check if the poster and qr codes are not default images
        onView(withId(R.id.iv_event_poster)).check(matches(not(withDrawable(R.drawable.image_placeholder_background))));
        onView(withId(R.id.ivCheckInQRCode)).check(matches(not(withDrawable(R.drawable.image_placeholder_background))));
        onView(withId(R.id.ivPromotionQRCode)).check(matches(not(withDrawable(R.drawable.image_placeholder_background))));

    }
}
