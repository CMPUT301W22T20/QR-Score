package com.example.qrscore;

import java.util.ArrayList;
import java.util.List;

/** Purpose: This class represents an account in the system.
 * Has score, total scanned, list of scanned QRs and a profile.
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
    private Integer score;
    private Integer scanned;

    /**
     * Purpose: Constructor for an account instance.
     *
     * @param userID
     *      The players unique user ID;
     */
    public Account(String userID) {
        this.userID = userID;
        this.profile = new Profile(userID);
        this.qrDataList = new ArrayList<QRCode>;
        this.score = 0;
        this.scanned = 0;
    }

    /**
     * Purpose: Constructor for an account instance.
     *
     * @param userID
     *      The players unique user ID.
     * @param score
     *      The players running total.
     * @param scanned
     *      The players total scanned QRs.
     */
    public Account(String userID, Integer score, Integer scanned) {
        this.userID = userID;
        this.profile = new Profile(userID);
        this.qrDataList = new ArrayList<QRCode>;
        this.scanned = scanned;
        this.score = score;
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
     * Purpose: Return the profile in the account.
     * @return
     *      A profile instance.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Purpose: Sets a profile of a user in the account.
     * @param profile
     *      A profile instance.
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Purpose: Return the running total score.
     *
     * @return
     *      Integer representing the total score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Purpose: Set the score for the account.
     *
     * @param score
     *      Integer representing the accounts score.
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * Purpose: Set the number of QR codes scanned.
     * @param scanned
     *      Integer representing the total QRs scanned.
     */
    public void setScanned(Integer scanned) {
        this.scanned = scanned;
    }

    /**
     * Purpose: Return the number of QR codes scanned.
     * @return
     *      Integer representing the number of QR codes scanned.
     */
    public Integer getScanned() {
        return scanned;
    }

    /**
     * Adds a QR code to the QRDataList.
     *
     * @param qr
     *      the QR code to add.
     */
    public void addQR(QRCode qr) {
        qrDataList.addQRCode(qr);
    }

    /**
     * Returns the list of QRCodes
     *
     * @return
     *      a List of QR codes.
     */
    public List<QRCode> getQR() {
        return qrDataList.getQRCodes();
    }

    /**
     * Purpose: Set the QRDataList for the account.
     * @param qrDataList
     *      Represents a QRDataList instance.
     */
    public void setQrDataList(QRDataList qrDataList) {
        this.qrDataList = qrDataList;
    }

    /**
     * Removes a QR code from the list if it exists.
     *
     * @param hash
     *      the hash of the QR code to remove.
     */
    public void removeQR(String hash) {
        qrDataList.removeQRCodeFromHash(hash);
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
     * Purpose: Return the highest scoring QR Code.
     *
     * @return
     *      Integer representing the highest scoring QR Code,
     */
    public Integer getHighest() {
        return qrDataList.getHighscore();
    }

    /**
     * Purpose: Get the lowest scoring QR Code
     *
     * @return
     *      Integer representing the lowest scoring QR Code.
     */
    public Integer getLowest() {
        return qrDataList.getLowscore();
    }

}
