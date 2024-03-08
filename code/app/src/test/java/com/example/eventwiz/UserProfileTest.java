package com.example.eventwiz;

import static junit.framework.TestCase.assertEquals;

import com.google.firebase.firestore.auth.User;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class that tests implementation of UserProfile class
 * @See: UserProfile.java
 */
public class UserProfileTest {
    UserProfile user;

    @Before
    public void BeforeAllTests() {
        user = new UserProfile("", "", "", "", "", "", "");
    }

    @Test
    /**
     * Test function that tests getter and setter methods of userName attribute
     */
    public void TestGetSetUserName() {
        String username_in_UserProfile = "";
        String username = "";
        //TEST CASE: No name given
        user.setUserName(username);
        username_in_UserProfile = user.getUserName();
        assertEquals(username, username_in_UserProfile);
        //TEST CASE: generic cases
        //1
        username = "Chinmoy Sahoo";
        user.setUserName(username);
        username_in_UserProfile = user.getUserName();
        assertEquals(username, username_in_UserProfile);
        //2
        username = "Colton Connick";
        user.setUserName(username);
        username_in_UserProfile = user.getUserName();
        assertEquals(username, username_in_UserProfile);
        //3
        username = "Hunaid Khan";
        user.setUserName(username);
        username_in_UserProfile = user.getUserName();
        assertEquals(username, username_in_UserProfile);
        //TEST CASE: SPECIAL CASES
        //1
        username = "     ";
        user.setUserName(username);
        username_in_UserProfile = user.getUserName();
        assertEquals(username, username_in_UserProfile);
        //2
        username = "..\n";
        user.setUserName(username);
        username_in_UserProfile = user.getUserName();
        assertEquals(username, username_in_UserProfile);
    }

    @Test
    /**
     * Test function that tests getter and setter methods of userEmail
     */
    public void TestGetSetUserEmail() {
        String useremail_in_UserProfile = "";
        String useremail = "";
        //TEST CASE: No name given
        user.setUserEmail(useremail);
        useremail_in_UserProfile = user.getUserEmail();
        assertEquals(useremail_in_UserProfile, useremail);
        //TEST CASE: generic cases
        //1
        useremail = "babawanga@seer.ca";
        user.setUserEmail(useremail);
        useremail_in_UserProfile = user.getUserEmail();
        assertEquals(useremail, useremail_in_UserProfile);
        //2
        useremail = "studentlife@ualberta.ca";
        user.setUserEmail(useremail);
        useremail_in_UserProfile = user.getUserEmail();
        assertEquals(useremail, useremail_in_UserProfile);
        //3
        useremail = "hightempemail@hotmail.com";
        user.setUserEmail(useremail);
        useremail_in_UserProfile = user.getUserEmail();
        assertEquals(useremail_in_UserProfile,useremail);
        //TEST CASE: SPECIAL CASES
        //1
        useremail = "\0null";
        user.setUserEmail(useremail);
        useremail_in_UserProfile = user.getUserEmail();
        assertEquals(useremail, useremail_in_UserProfile);
        //2
        useremail = "404ErRoR";
        user.setUserEmail(useremail);
        useremail_in_UserProfile = user.getUserEmail();
        assertEquals(useremail, useremail_in_UserProfile);
    }

    @Test
    /**
     * Test function that tests getter and setter methods of userHomepage attribute
     */
    public void TestGetSetUserHomepage() {
        String homepage_in_UserProfile = "";
        String homepage = "";
        //TEST CASE: No name given
        user.setUserHomepage(homepage);
        homepage_in_UserProfile = user.getUserHomepage();
        assertEquals(homepage_in_UserProfile, homepage);
        //TEST CASE: generic cases
        //1
        homepage = "www.mywebsite.com";
        user.setUserHomepage(homepage);
        homepage_in_UserProfile = user.getUserHomepage();
        assertEquals(homepage_in_UserProfile, homepage);
        //2
        homepage = "www.websitenameofaverylongwebsite.com";
        user.setUserHomepage(homepage);
        homepage_in_UserProfile = user.getUserHomepage();
        assertEquals(homepage, homepage_in_UserProfile);
        //3
        homepage = "https//:www.themagicshowhomepage.com";
        user.setUserHomepage(homepage);
        homepage_in_UserProfile = user.getUserHomepage();
        assertEquals(homepage_in_UserProfile, homepage);
        //TEST CASE: SPECIAL CASES
        //1
        homepage = "www.\nweirdsite\nwithnewlines\n.com";
        user.setUserHomepage(homepage);
        homepage_in_UserProfile = user.getUserHomepage();
        assertEquals(homepage, homepage_in_UserProfile);
        //2
        homepage = "0000000000";
        user.setUserHomepage(homepage);
        homepage_in_UserProfile = user.getUserHomepage();
        assertEquals(homepage, homepage_in_UserProfile);
    }

    @Test
    /**
     * Test function that tests getter and setter methods of userMobile
     */
    public void TestGetSetUserMobile() {
        String usermobile_in_UserProfile = "";
        String usermobile = "";
        //TEST CASE: No name given
        user.setUserMobile(usermobile);
        usermobile_in_UserProfile = user.getUserMobile();
        assertEquals(usermobile, usermobile_in_UserProfile);
        //TEST CASE: generic cases
        //1
        usermobile = "911";
        user.setUserMobile(usermobile);
        usermobile_in_UserProfile = user.getUserMobile();
        assertEquals(usermobile, usermobile_in_UserProfile);
        //2
        usermobile = "8685831111";
        user.setUserMobile(usermobile);
        usermobile_in_UserProfile = user.getUserMobile();
        assertEquals(usermobile, usermobile_in_UserProfile);
        //3
        usermobile = "5429520335";
        user.setUserMobile(usermobile);
        usermobile_in_UserProfile = user.getUserMobile();
        assertEquals(usermobile, usermobile_in_UserProfile);
        //TEST CASE: SPECIAL CASES
        //1
        usermobile = "nononononononono";
        user.setUserMobile(usermobile);
        usermobile_in_UserProfile = user.getUserMobile();
        assertEquals(usermobile, usermobile_in_UserProfile);
        //2
        usermobile = "...\t...\n.\0";
        user.setUserMobile(usermobile);
        usermobile_in_UserProfile = user.getUserMobile();
        assertEquals(usermobile, usermobile_in_UserProfile);
    }

    @Test
    /**
     * Test function that tests getter and setter methods of UserDocID attribute
     */
    public void TestGetSetUserDocID() {
        String userDocID_in_UserProfile = "";
        String userDocID = "";
        //TEST CASE: No name given
        user.setUserDocID(userDocID);
        userDocID_in_UserProfile = user.getUserDocID();
        assertEquals(userDocID, userDocID_in_UserProfile);
        //TEST CASE: generic cases
        //1
        userDocID = "0IITbMWv8LUtw2NHW73gheodBd3";
        user.setUserDocID(userDocID);
        userDocID_in_UserProfile = user.getUserDocID();
        assertEquals(userDocID, userDocID_in_UserProfile);
        //2
        userDocID = "4YINbMWg8LRtg5NHR23gGeodBd3";
        user.setUserDocID(userDocID);
        userDocID_in_UserProfile = user.getUserDocID();
        assertEquals(userDocID, userDocID_in_UserProfile);
        //3
        userDocID = "8IMKmMTv3LUjw2NHY68gbeedBd6";
        user.setUserDocID(userDocID);
        userDocID_in_UserProfile = user.getUserDocID();
        assertEquals(userDocID, userDocID_in_UserProfile);
        //TEST CASE: SPECIAL CASES
        //1
        userDocID = "00000000000\0\0";
        user.setUserDocID(userDocID);
        userDocID_in_UserProfile = user.getUserDocID();
        assertEquals(userDocID, userDocID_in_UserProfile);
        //2
        userDocID = "\n;;''";
        user.setUserDocID(userDocID);
        userDocID_in_UserProfile = user.getUserDocID();
        assertEquals(userDocID, userDocID_in_UserProfile);
    }

    @Test
    /**
     * Test function that tests getter and setter methods of CurrentUserID attribute
     */
    public void TestGetSetCurrentUserID() {
        String userID_in_UserProfile = "";
        String userID = "";
        //TEST CASE: No name given
        user.setCurrentUserID(userID);
        userID_in_UserProfile = user.getCurrentUserID();
        assertEquals(userID, userID_in_UserProfile);
        //TEST CASE: generic cases
        //1
        userID = "5GPKwMGg4FUyt9ULK73olyrhDVl1";
        user.setCurrentUserID(userID);
        userID_in_UserProfile = user.getCurrentUserID();
        assertEquals(userID, userID_in_UserProfile);
        //2
        userID = "2WPKwQTg4YUrt9ZMK99dntfhDVe6";
        user.setCurrentUserID(userID);
        userID_in_UserProfile = user.getCurrentUserID();
        assertEquals(userID, userID_in_UserProfile);
        //3
        userID = "7GXEfLTr3OIbh2URK22lagrrFVl1";
        user.setCurrentUserID(userID);
        userID_in_UserProfile = user.getCurrentUserID();
        assertEquals(userID, userID_in_UserProfile);
        //TEST CASE: SPECIAL CASES
        //1
        userID = "\n       \n";
        user.setCurrentUserID(userID);
        userID_in_UserProfile = user.getCurrentUserID();
        assertEquals(userID, userID_in_UserProfile);
        //2
        userID = "-----------";
        user.setCurrentUserID(userID);
        userID_in_UserProfile = user.getCurrentUserID();
        assertEquals(userID, userID_in_UserProfile);
    }
}
