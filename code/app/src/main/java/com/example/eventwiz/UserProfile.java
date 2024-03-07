package com.example.eventwiz;

/**
 * Responsible for storing the User's profile
 * @author Yesith
 */
public class UserProfile {
    private String UserName, UserEmail, UserHomepage, UserMobile,UserDocID,CurrentUserID,ProfilePicImage;


    /**
     * Initializes the parameters used in the UserProfile class
     * @param userName
     * @param userEmail
     * @param userHomepage
     * @param userMobile
     * @param userDocID
     * @param currentUserID
     * @param profilePicImage
     */
    public UserProfile(String userName, String userEmail, String userHomepage, String userMobile, String userDocID, String currentUserID, String profilePicImage) {
        UserName = userName;
        UserEmail = userEmail;
        UserHomepage = userHomepage;
        UserMobile = userMobile;
        UserDocID = userDocID;
        CurrentUserID = currentUserID;
        ProfilePicImage = profilePicImage;
    }

    /**
     *
     * @return user name as a string
     */
    public String getUserName() {
        return UserName;
    }

    /**
     * Sets user name
     * @param userName
     */
    public void setUserName(String userName) {
        UserName = userName;
    }

    /**
     *
     * @return user email as a string
     */
    public String getUserEmail() {
        return UserEmail;
    }

    /**
     * Sets user email
     * @param userEmail
     */
    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    /**
     *
     * @return user homepage as a string
     */
    public String getUserHomepage() {
        return UserHomepage;
    }

    /**
     * Sets user homepage
     * @param userHomepage
     */
    public void setUserHomepage(String userHomepage) {
        UserHomepage = userHomepage;
    }

    /**
     *
     * @return user mobile as a string
     */
    public String getUserMobile() {
        return UserMobile;
    }

    /**
     * Sets user mobile
     * @param userMobile
     */
    public void setUserMobile(String userMobile) {
        UserMobile = userMobile;
    }

    /**
     *
     * @return user DocID as a string
     */
    public String getUserDocID() {
        return UserDocID;
    }

    /**
     * Sets user DocID
     * @param userDocID
     */
    public void setUserDocID(String userDocID) {
        UserDocID = userDocID;
    }

    /**
     *
     * @return current user ID as a string
     */
    public String getCurrentUserID() {
        return CurrentUserID;
    }

    /**
     * Sets Current user ID as a string
     * @param currentUserID
     */
    public void setCurrentUserID(String currentUserID) {
        CurrentUserID = currentUserID;
    }

    /**
     *
     * @return profile pic image
     */
    public String getProfilePicImage() {
        return ProfilePicImage;
    }

    /**
     * Sets profile pic image
     * @param profilePicImage
     */
    public void setProfilePicImage(String profilePicImage) {
        ProfilePicImage = profilePicImage;
    }
}
