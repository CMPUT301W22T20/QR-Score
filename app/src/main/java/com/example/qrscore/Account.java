package com.example.qrscore;

import android.util.Log;

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
    private Profile profile;
    private QRDataList qrDataList;
    private Integer totalScore;
    private Integer scanned;

    public Account(String userID) {
        this.userID = userID;
        this.profile = new Profile(userID);
        this.qrDataList = new QRDataList();
        this.totalScore = 0;
        this.scanned = 0;
    }

    public Account(String userID, Integer totalScore, Integer scanned) {
        this.userID = userID;
        this.profile = new Profile(userID);
        this.qrDataList = new QRDataList();
        this.totalScore = totalScore;
        this.scanned = scanned;
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

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public void setScanned(Integer scanned) {
        this.scanned = scanned;
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

    public void setQrDataList(QRDataList qrDataList) {
        this.qrDataList = qrDataList;
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

    /**
     * Returns the QRDataList
     *
     * @return
     *      qrDataList
     */
    public QRDataList getQrDataList() {
        return qrDataList;
    }

    /**
     * Returns the username of the user
     *
     * @return
     *      username
     */
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
        return profile.getUserUID();
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
