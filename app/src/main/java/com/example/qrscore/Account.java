package com.example.qrscore;

// TODO: As a player, I want to add new QR codes to my account.
//  As a player, I want to see what QR codes I have added to my account.
//  As a player, I want to remove QR codes from my account.
//  As a player, I want to see my highest and lowest scoring QR codes.

// TODO: UI--navbar with "add" button to add QR codes.

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
    private QRList qrList;
    // private Permissions permissions;

    public Account(String userID, String device) {
        this.userID = userID;
        this.device = device;
        this.qrList = new QRList();
    }

    public String getUserID() {
        return userID;
    }

    public String getDevice() {
        return device;
    }

    public void addQR(QRCode qr) {
        qrList.addQRCode(qr);
    }

    public List<QRCode> getQR() {
        return qrList.getQRCodes();
    }

    public void removeQR(QRCode qr) {
        qrList.removeQRCode(qr);
    }

//    public QRCode getHighest() {
//        return stats.getHighscore();
//    }

//    public QRCode getLowest() {
//        return stats.getLowscore();
//    }
}
