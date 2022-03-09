package com.example.qrscore;

import java.util.List;

//  As a player, I want to see that other players have scanned the same QR code.
public class Account {
    private List<QRCode> qrCodes;
    private String device;
    private Profile profile;
    // private Stats stats;
    // private Permissions permissions;

    public List<QRCode> getQRCodes() {
        return qrCodes;
    }

    public void addQRCode(QRCode toAdd) {
        qrCodes.add(toAdd);
    }

    public void removeQRCode(QRCode toRemove) {
        qrCodes.remove(toRemove);
    }

    /**
     * Constructor for the account
     *
     * @param profile
     *      The account's profile
     */
    public Account(Profile profile) {
        this.profile = profile;
    }

    /** Gets the Username of the profile
     *
     * @return
     *      username of the profile
     */
    public String getUsername() {
        return profile.getUsername();
    }

}
