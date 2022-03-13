package com.example.qrscore;

/**
 * Purpose: This class represents a user's public profile.
 *
 * Outstanding issues:
 * TODO: Finish Purpose
 * TODO: Unit tests
 * TODO: Javadocs
 * TODO: Merge with William's Profile
 */
public class Profile {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phoneNumber;

    public Profile(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

}
