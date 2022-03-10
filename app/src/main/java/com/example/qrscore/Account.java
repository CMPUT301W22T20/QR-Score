package com.example.qrscore;

import java.util.List;

//  As a player, I want to see that other players have scanned the same QR code.

/* Purpose: This class represents an account in the system.
 * Stores the QR codes tied to the account, the device,
 * and the profile.
 *
 * Outstanding issues:
 */

public class Account {
    private String device;
    private Profile profile;
    private Stats stats;
    List<QRCode> qrCodes;
    // private Permissions permissions;


    /**
     * Constructor for the account
     *
     * @param profile
     *      The account's profile
     */
    public Account(Profile profile) {
        this.profile = profile;
    }

    /**
     * Gets the Username of the profile
     *
     * @return
     *      username of the profile
     */
    public String getUsername() {
        return profile.getUsername();
    }

    /**
     * Gets account QR codes
     *
     * @return
     *      list of QR Codes
     */
    public List<QRCode> getQRCodes() {
        return qrCodes;
    }

    /**
     * Adds QR code to account
     *
     * @param toAdd
     *      The QR Code to add
     */
    public void addQRCode(QRCode toAdd) {
        qrCodes.add(toAdd);
    }

    /**
     * Removes QR code from account
     *
     * @param toRemove
     *      The QR code to remove
     */
    public void removeQRCode(QRCode toRemove) {
        qrCodes.remove(toRemove);
    }


}
