package com.example.qrscore;

import java.util.ArrayList;
import java.util.List;

/** Purpose: This class represents an account in the system.
 * Stores the QR codes tied to the account, the device,
 * and the profile.
 *
 * Outstanding issues:
 * TODO: Finish Purpose
 * TODO: As a player, I want to remove QR codes from my account.
 * TODO: As a player, I want to see my highest and lowest scoring QR codes.
 * TODO: Unit tests
 * TODO: UI--navbar with "add" button to add QR codes.
 */
public class Account {
    private static final String TAG = "ACCOUNT";
    private String userID;
    public Profile profile;
    public QRDataList qrDataList;
    public Integer totalScore;
    public Integer scanned;
    public String username;
// private Permissions permissions;

    public Account(String userID) {
        this.userID = userID;
        this.profile = new Profile(userID);
        this.qrDataList = new QRDataList();
        this.totalScore = 0;
        this.scanned = 0;
        this.username = "Default";
    }

    public Account(String userID, Integer totalScore, Integer scanned) {
        this.userID = userID;
        this.profile = new Profile(userID);
        this.qrDataList = new QRDataList();
        this.totalScore = totalScore;
        this.scanned = scanned;
        this.username = "Default";
    }

    /**
     * Returns the Account's user ID.
     *
     * @return
     *      user ID as a string.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Adds a QR code to the list.
     *
     * @param qr
     *      the QR code to add.
     */
    public void addQR(QRCode qr) {
        qrDataList.addQRCode(qr);
    }

    /**
     * Returns the array of QR codes.
     *
     * @return
     *      a List of QR codes.
     */
    public List<QRCode> getQR() {
        return qrDataList.getQRCodes();
    }

    /**
     * Removes a QR code from the list if it exists.
     *
     * @param qr
     *      the QR code to remove.
     */
    public void removeQR(QRCode qr) {
        qrDataList.removeQRCode(qr);
    }

    public Integer getHighest() {
        return qrDataList.getHighscore();
    }

    public Integer getLowest() {
        return qrDataList.getLowscore();
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public Integer getScanned() {
        return scanned;
    }

    public String getUsername() {
        return username;
    }
}
