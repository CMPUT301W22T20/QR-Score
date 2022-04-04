package com.example.qrscore.model;

/**
 * Purpose: This class is used to store profile info. (name, email, phone, username,etc.)
 *
 * Outstanding Issues:
 *
 * @author William Liu/Matthew Braun
 */
public class Profile {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private final String userUID;
    private Boolean permanent;

    /**
     * Purpose: Empty Constructor for a profile.
     */
    public Profile() {
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.phoneNumber = null;
        this.userUID = null;
        this.permanent = false;
    }

    /**
     * Purpose: Constructor for new authenticators.
     *
     * @param userUID
     *      Represents a unique username/identifier.
     */
    public Profile(String userUID) {
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.phoneNumber = null;
        this.userUID = userUID;
        this.permanent = false;
    }

    /**
     * Purpose: Constructor for populated profiles.
     *
     * @param firstName
     *      Represents the players first name.
     * @param lastName
     *      Represents the players last name.
     * @param email
     *      Represents the players email.
     * @param phoneNumber
     *      Represents the players phoneNumber.
     * @param userUID
     *      Represents a unique username/identifier.
     */
    public Profile(String firstName, String lastName, String email, String phoneNumber, String userUID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userUID = userUID;
        this.permanent = false;
    }

    /**
     * Purpose:
     * - Constructor for populated profiles.
     * - AND if the profile is permanent (i.e. can transfer ownership of profiles on different devices).
     *
     * @param firstName
     *      Represents the players first name.
     * @param lastName
     *      Represents the players last name.
     * @param email
     *      Represents the players email.
     * @param phoneNumber
     *      Represents the players phoneNumber.
     * @param userUID
     *      Represents a unique username/identifier.
     * @param permanent
     *      Represents if the profile is permanent.
     */
    public Profile(String firstName, String lastName, String email, String phoneNumber, String userUID, boolean permanent) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.userUID = userUID;
            this.permanent = permanent;
    }

    /**
     * Purpose: Returns the players first name.
     *
     * @return
     *      Represents the players first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Purpose: Sets the players first name.
     *
     * @param firstName
     *      Represents the players first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Purpose: Returns the players last name.
     *
     * @return
     *      Represents the players last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Purpose: Sets the players last name.
     *
     * @param lastName
     *      Represents the players last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Purpose: Returns the players email.
     *
     * @return
     *      Represents the players email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Purpose: Sets the players email.
     *
     * @param email
     *      Set the players email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Purpose: Returns the players phone number.
     *
     * @return
     *      Represents the players phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Purpose: Sets the players phone number.
     *
     * @param phoneNumber
     *      Set the players phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Purpose: Returns the players unique userID.
     *
     * @return
     *      Represents the players unique userID.
     */
    public String getUserUID() {
        return userUID;
    }

    /**
     * Purpose: Returns if the profile is permanent
     *
     * @return
     *      Represents if the user can login by QR Code.
     */
    public Boolean getPermanent() {
        return permanent;
    }

    /**
     * Purpose: Set if the players profile is permanent
     *
     * @param permanent
     *      Boolean representing if user can login by QR Code.
     */
    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

}


