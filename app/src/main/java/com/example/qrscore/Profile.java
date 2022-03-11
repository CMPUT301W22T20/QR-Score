package com.example.qrscore;

/**
 * Purpose: This class is used to store profile info. (name, email, phone, username,etc.)
 * It should also be able to display a QR code to transfer devices.
 *
 * TODO: Implement QR Code.
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

    /**
     * Purpose: Empty Constructor for a profile.
     */
    public Profile() {
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.phoneNumber = null;
        this.userUID = null;
    }

    /**
     * Purpose: Constructor for new authenticators.
     * @param userUID
     *      Represents a unique username/identifier.
     */
    public Profile(String userUID) {
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.phoneNumber = null;
        this.userUID = userUID;
    }

    /**
     * Purpose: Constructor for populated profiles.
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
    }

    /**
     * Returns the players first name.
     * @return
     *      Represents the players first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the players first name.
     * @param firstName
     *      Represents the players first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the players last name.
     * @return
     *      Represents the players last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the players last name.
     * @param lastName
     *      Represents the players last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the players email.
     * @return
     *      Represents the players email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the players email.
     * @param email
     *      Set the players email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the players phone number.
     * @return
     *      Represents the players phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the players phone number,
     * @param phoneNumber
     *      Set the players phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the players unique userID.
     * @return
     *      Represents the players unique userID.
     */
    public String getUserUID() {
        return userUID;
    }

}
