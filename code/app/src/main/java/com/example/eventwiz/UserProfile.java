package com.example.eventwiz;

public class UserProfile {
    private String UserName, UserEmail, UserHomepage, UserMobile,UserDocID,CurrentUserID,ProfilePicImage;

    public UserProfile() {
    }

    public UserProfile(String userName, String userEmail, String userHomepage, String userMobile, String userDocID, String currentUserID, String profilePicImage) {
        UserName = userName;
        UserEmail = userEmail;
        UserHomepage = userHomepage;
        UserMobile = userMobile;
        UserDocID = userDocID;
        CurrentUserID = currentUserID;
        ProfilePicImage = profilePicImage;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserHomepage() {
        return UserHomepage;
    }

    public void setUserHomepage(String userHomepage) {
        UserHomepage = userHomepage;
    }

    public String getUserMobile() {
        return UserMobile;
    }

    public void setUserMobile(String userMobile) {
        UserMobile = userMobile;
    }

    public String getUserDocID() {
        return UserDocID;
    }

    public void setUserDocID(String userDocID) {
        UserDocID = userDocID;
    }

    public String getCurrentUserID() {
        return CurrentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        CurrentUserID = currentUserID;
    }

    public String getProfilePicImage() {
        return ProfilePicImage;
    }

    public void setProfilePicImage(String profilePicImage) {
        ProfilePicImage = profilePicImage;
    }
}
