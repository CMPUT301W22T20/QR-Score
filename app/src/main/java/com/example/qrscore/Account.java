package com.example.qrscore;

// TODO: As a player, I want to add new QR codes to my account.
//  As a player, I want to see what QR codes I have added to my account.
//  As a player, I want to remove QR codes from my account.
//  As a player, I want to see my highest and lowest scoring QR codes.

// TODO: UI--navbar with "add" button to add QR codes.

import android.util.Log;

import java.util.List;

/* Purpose: This class represents an account in the system.
 * Stores the QR codes tied to the account, the device,
 * and the profile.
 *
 * Outstanding issues:
 */
public class Account {
    private static final String TAG = "ACCOUNT";
    private String userID;
    private String device;
    private Profile profile;
    private Stats stats; // Rename Stats to QRList?
    // private Permissions permissions;

    public Account(String userID, String device) {
        this.userID = userID;
        this.device = device;
        this.stats = new Stats();
    }

    public String getUserID() {
        return userID;
    }

    public String getDevice() {
        return device;
    }

    public void addQR(QRCode qr) {
        this.stats.addQRCode(qr);
    }

    public List<QRCode> getQR() {
        return this.stats.getQRCodes();
    }

    public void removeQR(QRCode qr) {
        this.stats.removeQRCode(qr);
    }

//    public QRCode getHighest() {
//        return stats.getHighscore();
//    }

//    public QRCode getLowest() {
//        return stats.getLowscore();
//    }
}
