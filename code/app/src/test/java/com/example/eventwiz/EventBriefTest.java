package com.example.eventwiz;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.*;

/**
 * Test class that aims to test the EventBrief class
 * @See: EventBrief.java
 */
public class EventBriefTest {
    //To store the EventBrief object
    private EventBrief event;

    @Before
    public void BeforeEveryTest() {
        event = new EventBrief("", "", "", "", "", "");
    }

    @Test
    /**
     * Function to test the getter and setter methods of eventName attribute
     */
    public void TestGetSetName() {
        String event_name_in_Event = "";
        //TEST CASE: When no name is provided
        String event_name = "";
        event.setEventName(event_name);
        event_name_in_Event = event.getEventName();
        assertEquals(event_name, event_name_in_Event);

        //TEST CASE: Generic test cases
        //1
        event_name = "My Event";
        event.setEventName(event_name);
        event_name_in_Event = event.getEventName();
        assertEquals(event_name, event_name_in_Event);
        //2
        event_name = "The Magic Show";
        event.setEventName(event_name);
        event_name_in_Event = event.getEventName();
        assertEquals(event_name, event_name_in_Event);
        //3
        event_name = "Movie Night";
        event.setEventName(event_name);
        event_name_in_Event = event.getEventName();
        assertEquals(event_name, event_name_in_Event);

        //TEST CASE: Special values
        //1
        event_name = ".";
        event.setEventName(event_name);
        event_name_in_Event = event.getEventName();
        assertEquals(event_name, event_name_in_Event);
        //2
        event_name = "This is the Name of an Event with a very large Event Name for testing purposes.";
        event.setEventName(event_name);
        event_name_in_Event = event.getEventName();
        assertEquals(event_name, event_name_in_Event);
        //3
        event_name = "\n\n";
        event.setEventName(event_name);
        event_name_in_Event = event.getEventName();
        assertEquals(event_name, event_name_in_Event);
        //4
        event_name = "\t\t";
        event.setEventName(event_name);
        event_name_in_Event = event.getEventName();
        assertEquals(event_name, event_name_in_Event);
        //5
        event_name = "\"";
        event.setEventName(event_name);
        event_name_in_Event = event.getEventName();
        assertEquals(event_name, event_name_in_Event);
    }

    @Test
    /**
     * Test function that tests getter and setter methods for eventDate attribute
     */
    public void TestGetSetEventDate() {
        String event_date_in_Event = "";
        //TEST CASE: no string provided
        String event_date = "";
        event.setEventDate(event_date);
        event_date_in_Event = event.getEventDate();
        assertEquals(event_date, event_date_in_Event);
        //TEST CASE: test different possible formats
        //1
        event_date = "mm/dd/yyyy";
        event.setEventDate(event_date);
        event_date_in_Event = event.getEventDate();
        assertEquals(event_date, event_date_in_Event);
        //2
        event_date = "04/30/2024";
        event.setEventDate(event_date);
        event_date_in_Event = event.getEventDate();
        assertEquals(event_date, event_date_in_Event);
        //3
        event_date = "30th April, 2024";
        event.setEventDate(event_date);
        event_date_in_Event = event.getEventDate();
        assertEquals(event_date, event_date_in_Event);
        //4
        event_date = "30/04/2024";
        event.setEventDate(event_date);
        event_date_in_Event = event.getEventDate();
        assertEquals(event_date, event_date_in_Event);

        //TEST CASE: Special Cases
        //1
        event_date = "....";
        event.setEventDate(event_date);
        event_date_in_Event = event.getEventDate();
        assertEquals(event_date, event_date_in_Event);
        //2
        event_date = "\n";
        event.setEventDate(event_date);
        event_date_in_Event = event.getEventDate();
        assertEquals(event_date, event_date_in_Event);
        //3
        event_date = "\0";
        event.setEventDate(event_date);
        event_date_in_Event = event.getEventDate();
        assertEquals(event_date, event_date_in_Event);
    }

    @Test
    /**
     * Test function that tests the getter and setter methods of StartTime attribute
     */
    public void TestGetSetStartTime() {
        String event_time_in_Event = "";
        //TEST CASE: No time given
        String event_time = "";
        event.setEventStartTime(event_time);
        event_time_in_Event = event.getEventStartTime();
        assertEquals(event_time, event_time_in_Event);
        //TEST CASE: Test different time formats
        //1
        event_time = "6:30 PM";
        event.setEventStartTime(event_time);
        event_time_in_Event = event.getEventStartTime();
        assertEquals(event_time, event_time_in_Event);
        //2
        event_time = "18:15";
        event.setEventStartTime(event_time);
        event_time_in_Event = event.getEventStartTime();
        assertEquals(event_time, event_time_in_Event);
        //3
        event_time = "0945";
        event.setEventStartTime(event_time);
        event_time_in_Event = event.getEventStartTime();
        assertEquals(event_time, event_time_in_Event);

        //TEST CASE: special cases
        //1
        event_time = "\n";
        event.setEventStartTime(event_time);
        event_time_in_Event = event.getEventStartTime();
        assertEquals(event_time, event_time_in_Event);
        //2
        event_time = "][[]";
        event.setEventStartTime(event_time);
        event_time_in_Event = event.getEventStartTime();
        assertEquals(event_time, event_time_in_Event);
    }

    @Test
    /**
     * Test function that tests the getter and setter methods of PosterURL attribute
     */
    public void TestGetSetPosterURL(){
        String posterurl_in_Event = "";
        //TEST CASE: when no url given
        String posterurl = "";
        event.setPosterUrl(posterurl);
        posterurl_in_Event = event.getPosterUrl();
        assertEquals(posterurl_in_Event, posterurl);
        //TEST CASE: generic url cases
        //1
        posterurl = "https//:www.myposter.com";
        event.setPosterUrl(posterurl);
        posterurl_in_Event = event.getPosterUrl();
        assertEquals(posterurl, posterurl_in_Event);
        //2
        posterurl = "https//:www.thisisposterurl.com";
        event.setPosterUrl(posterurl);
        posterurl_in_Event = event.getPosterUrl();
        assertEquals(posterurl, posterurl_in_Event);
        //3
        posterurl = "www.anotherposterurl.com";
        event.setPosterUrl(posterurl);
        posterurl_in_Event = event.getPosterUrl();
        assertEquals(posterurl, posterurl_in_Event);
        //TEST CASE: special cases
        //1
        posterurl = "https//:www.\n.com";
        event.setPosterUrl(posterurl);
        posterurl_in_Event = event.getPosterUrl();
        assertEquals(posterurl, posterurl_in_Event);
        //2
        posterurl = "https//my\tposter.com";
        event.setPosterUrl(posterurl);
        posterurl_in_Event = event.getPosterUrl();
        assertEquals(posterurl, posterurl_in_Event);
    }

    @Test
    /**
     * Test function that tests the getter and setter methods of Location attribute
     */
    public void TestGetSetLocation() {
        String venue_in_Event = "";
        //TEST CASE: no venue provided
        String venue = "";
        event.setLocation(venue);
        venue_in_Event = event.getLocation();
        assertEquals(venue, venue_in_Event);
        //TEST CASE: generic address formats
        //1
        venue = "99199 82nd Ave NW, \n Edmonton, Alberta T6G XXX";
        event.setLocation(venue);
        venue_in_Event = event.getLocation();
        assertEquals(venue, venue_in_Event);
        //2
        venue = "30042003 37th Ave NW, \n Edmonton, Alberta,\n T6G 4T0";
        event.setLocation(venue);
        venue_in_Event = event.getLocation();
        assertEquals(venue, venue_in_Event);
        //3
        venue = "CCIS 112th St NW, Edmonton, Alberta T6G XXX";
        event.setLocation(venue);
        venue_in_Event = event.getLocation();
        assertEquals(venue, venue_in_Event);
        //TEST CASE: special case
        //1
        venue = "here\nthere\neverywhere";
        event.setLocation(venue);
        venue_in_Event = event.getLocation();
        assertEquals(venue, venue_in_Event);
        //2
        venue = "7588920477756699\t?";
        event.setLocation(venue);
        venue_in_Event = event.getLocation();
        assertEquals(venue, venue_in_Event);
    }

}
