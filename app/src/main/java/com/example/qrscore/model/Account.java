package com.example.qrscore.model;

import java.util.ArrayList;
import java.util.List;

/** Purpose: This class represents an account in the system.
 * Has score, total totalScanned, list of totalScanned QRs and a profile.
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
    private String userUID;
    private Profile profile;
    private ArrayList<QRCode> qrCodes;
    private boolean isOwner;
    private String totalScore;
    private String totalScanned;
    private String hiscore;
    private String rankTotalScore;
    private String rankTotalScanned;
    private String rankHiscore;

    /**
     * Purpose: Empty Constructor for a account.
     */
    public Account() {
        this.userUID = null;
        this.profile = null;
        this.qrCodes = null;
        this.totalScore = null;
        this.totalScanned = null;
        this.hiscore = null;
        this.rankTotalScore = null;
        this.rankTotalScanned = null;
        this.rankHiscore = null;
    }

    /**
     * Purpose: Constructor for an account instance.
     *
     * @param userUID
     *      The players unique user ID;
     */
    public Account(String userUID) {
        this.userUID = userUID;
        this.profile = new Profile(userUID);
        this.qrCodes = new ArrayList<>();
        this.totalScore = "0";
        this.hiscore = "0";
        this.totalScanned = "0";
        this.rankTotalScore = "0";
        this.rankTotalScanned = "0";
        this.rankHiscore = "0";
    }

    /**
     * Purpose: Constructor for an account instance.
     *
     * @param userUID
     *      The player's unique user ID.
     * @param totalScore
     *      The player's running total score.
     * @param totalScanned
     *      The player's total totalScanned QRs.
     */
    public Account(String userUID, String totalScore, String totalScanned, String hiscore,
                   String rankTotalScore, String rankTotalScanned, String rankHiscore) {
        this.userUID = userUID;
        this.profile = new Profile(userUID);
        this.qrCodes = new ArrayList<>();
        this.totalScore = totalScore;
        this.totalScanned = totalScanned;
        this.hiscore = hiscore;
        this.rankTotalScore = rankTotalScore;
        this.rankTotalScanned = rankTotalScanned;
        this.rankHiscore = rankHiscore;
    }

    /**
     * Returns the Account's user ID.
     *
     * @return
     *      user ID as a string.
     */
    public String getUserUID() {
        return userUID;
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
    public String getTotalScore() {
        return totalScore;
    }

    /**
     * Purpose: Set the score for the account.
     *
     * @param totalScore
     *      String representing the accounts score.
     */
    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * Purpose: Return the number of QR codes totalScanned.
     * @return
     *      String representing the number of QR codes totalScanned.
     */
    public String getTotalScanned() {
        return totalScanned;
    }
//        return qrCodes.size();

    /**
     * Purpose: Set the number of QR codes totalScanned.
     * @param totalScanned
     *      String representing the total QRs totalScanned.
     */
    public void setTotalScanned(String totalScanned) {
        this.totalScanned = totalScanned;
    }

    /**
     * Purpose: Return the highest individual QR score for a player
     * @return
     *      String representing highest individual QR score.
     */
    public String getHiscore() {
        return hiscore;
    }

    /**
     * Purpose: Set the highest individual QR score for a player.
     * @param hiscore
     *      String representing the highest individual QR score.
     */
    public void setHiscore(String hiscore) {
        this.hiscore = hiscore;
    }

    /**
     * Purpose: Return the running total score rank.
     *
     * @return
     *      String representing the total score rank
     */
    public String getRankTotalScore() {
        return rankTotalScore;
    }

    /**
     * Purpose: Set the score rank for the account.
     *
     * @param rankTotalScore
     *      String representing the accounts score rank.
     */
    public void setRankTotalScore(String rankTotalScore) {
        this.rankTotalScore = rankTotalScore;
    }

    /**
     * Purpose: Return the rank of total QR codes scanned.
     * @return
     *      String representing the player's rank of total QR codes scanned.
     */
    public String getRankTotalScanned() {
        return this.rankTotalScanned;
    }
//        return qrCodes.size();

    /**
     * Purpose: Set the player's rank of QR codes scanned.
     * @param rankTotalScanned
     *      String representing the rank of total QRs scanned.
     */
    public void setRankTotalScanned(String rankTotalScanned) {
        this.rankTotalScanned = rankTotalScanned;
    }

    /**
     * Purpose: Return the highest individual QR score for a player
     * @return
     *      String representing highest individual QR score.
     */
    public String getRankHiscore() {
        return this.rankHiscore;
    }

    /**
     * Purpose: Set the highest individual QR score rank for a player.
     * @param rankHiscore
     *      String representing the highest individual QR score's rank.
     */
    public void setRankHiscore(String rankHiscore) {
        this.rankHiscore = rankHiscore;
    }

    /**
     * Returns the list of QRCodes
     *
     * @return
     *      a List of QR codes.
     */
    public List<QRCode> getQRCodesList() {
        return qrCodes;
    }

    public void setQRCodesList(ArrayList<QRCode> qrCodesArray) {
        this.qrCodes = qrCodesArray;
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

    /**
     * Returns if the account is an owner or not
     *
     * @return
     *      true if account is an owner, false otherwise
     */
    public boolean isOwner() {
        return isOwner;
    }

    /**
     * Sets the owner status of the account to true
     *
     * @param owner
     *      true
     */
    public void setOwner(boolean owner) {
        isOwner = owner;
    }



//    private void calculateTotalScore() {
//        int sum = 0;
//        for (QRCode qrCode: qrCodes) {
//            sum = sum + qrCode.getQRScore();
//        }
//        score = sum;
//    }
}
