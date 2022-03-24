package com.example.qrscore.model;

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
    private ArrayList<QRCode> qrCodes;
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
        this.score = 0;
        this.scanned = 0;
        this.qrCodes = new ArrayList<>();
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
        this.scanned = scanned;
        this.score = score;
        this.qrCodes = new ArrayList<>();
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
//        calculateTotalScore();
//        return score;
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
        return qrCodes.size();
    }

    /**
     * Returns the list of QRCodes
     *
     * @return
     *      a List of QR codes.
     */
    public List<QRCode> getQRList() {
        return qrCodes;
    }

    public QRCode getQRByHash(String hash) {
        for (QRCode qrCode: qrCodes) {
            if (qrCode.getHash().equals(hash)) {
                return qrCode;
            }
        }
        return null;
    }

    /**
     * Removes a QR code from the list if it exists.
     *
     * @param hash
     *      the hash of the QR code to remove.
     */
    public void removeQR(String hash) {
        for (QRCode qrCode: qrCodes) {
            if (qrCode.getHash() == hash) {
                qrCodes.remove(qrCode);
                return;
            }
        }
    }

    public void setQRCodesList(ArrayList<QRCode> qrCodesArray) {
        this.qrCodes = qrCodesArray;
    }

//    private void calculateTotalScore() {
//        int sum = 0;
//        for (QRCode qrCode: qrCodes) {
//            sum = sum + qrCode.getQRScore();
//        }
//        score = sum;
//    }
}
