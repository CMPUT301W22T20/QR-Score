package com.example.qrscore;

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
