package com.example.qrscore;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Purpose: This class contains a list of a player's QR Codes and their respective stats.
 * Stores a list of QR codes the player has scanned
 * Stores the total # of QR codes scanned
 * Stores the score sum of the player's scanned QR codes
 * Stores the player's rank relative to other players
 *
 * Outstanding issues:
 * TODO: implement ranking system
 * TODO: connect to firebase for ranks
 * TODO: Unit tests
 * TODO: Highscore/Lowscore should probably return QRCodes so they can be displayed?
 */
public class QRDataList {
    final String TAG = "QRDataList";
    private ArrayList<QRCode> qrCodes;
    private Integer totalQRCodesScanned;
    private Integer sumOfScoresScanned;
    private Integer rank;


    /**
     * Constructor for Stats class
     *
     */
    public QRDataList() {
        this.qrCodes = new ArrayList<QRCode>();
        this.rank = 0;
        this.sumOfScoresScanned = 0;
        this.totalQRCodesScanned = 0;
    }

    public QRDataList(Integer totalQRCodesScanned, Integer sumOfScoresScanned, Integer rank) {
        this.totalQRCodesScanned = totalQRCodesScanned;
        this.sumOfScoresScanned = sumOfScoresScanned;
        this.rank = rank;
    }

    public QRDataList(ArrayList<QRCode> qrCodes, Integer rank, Integer sumOfScoresScanned, Integer totalQRCodesScanned) {
        this.qrCodes = qrCodes;
        this.rank = rank;
        this.sumOfScoresScanned = sumOfScoresScanned;
        this.totalQRCodesScanned = totalQRCodesScanned;
    }

    /**
     * Returns the array of QR codes.
     *
     * @return
     *      a List of QR codes.
     */
    public ArrayList<QRCode> getQRCodes() {
        return qrCodes;
    }

    public void setQRCodes(ArrayList<QRCode> qrCodes) {
        this.qrCodes = qrCodes;
    }
    /**
     * Adds a QR code to the list.
     *
     * @param toAdd
     *      the QR code to add.
     */
    public void addQRCode(QRCode toAdd) {
        qrCodes.add(toAdd);
        this.updateStats();
        Log.i(TAG, "toAdd.qrscore: " + toAdd.getQRScore());
        System.out.println("qr hash: " + toAdd.getHash());
        System.out.println("qrCodes.size(): " + qrCodes.size());
        System.out.println("this.getTotalQRCodesScanned(): " + this.getTotalQRCodesScanned());
    }

    /**
     * Removes a QR code from the list if it exists.
     *
     * @param toRemove
     *      the QR code to remove.
     */
    public void removeQRCode(QRCode toRemove) {
        // Check if the QR code is in the list or not, throw exception if it isn't.
        qrCodes.remove(toRemove);
        this.updateStats();
    }

    /**
     * Returns the total number of QR codes a user has scanned.
     *
     * @return
     *      the sum of a user's scanned QR codes.
     */
    public void setTotalQRCodesScanned(Integer newTotal) {
        this.totalQRCodesScanned = newTotal;
    }

    /**
     * Returns the total sum of QR scores a user has scanned.
     *
     * @return
     *      the sum of a user's scanned QR scores.
     */
    public void setSumOfScoresScanned(Integer newSum) {
        this.sumOfScoresScanned = newSum;
    }

    /**
     * Returns the total number of QR codes a user has scanned.
     *
     * @return
     *      the sum of a user's scanned QR codes.
     */
    public Integer getTotalQRCodesScanned() {
        return this.totalQRCodesScanned;
    }

    /**
     * Returns the total sum of QR scores a user has scanned.
     *
     * @return
     *      the sum of a user's scanned QR scores.
     */
    public Integer getSumOfScoresScanned() {
        return this.sumOfScoresScanned;
    }

    /**
     * Returns the total sum of QR scores a user has scanned.
     *
     * @return
     *      the user's rank relative to all other users.
     */
    public Integer getRank() {
        return this.rank;
    }

    /**
     * Returns the highest QR score
     * @return
     *      the player's highest scoring QR code's score
     */
    public Integer getHighscore() {
        List<Integer> qrScores = new ArrayList<Integer>();
        for (int i=0; i<qrCodes.size(); i++) {
            qrScores.add(qrCodes.get(i).getQRScore());
        }
        Collections.sort(qrScores);

        return qrScores.get(qrScores.size() - 1);
    }

    /**
     * Returns the lowest QR score
     * @return
     *      the player's lowest scoring QR code's score
     */
    public Integer getLowscore() {
        List<Integer> qrScores = new ArrayList<Integer>();
        for (int i=0; i<qrCodes.size(); i++) {
            qrScores.add(qrCodes.get(i).getQRScore());
        }
        Collections.sort(qrScores);

        return qrScores.get(0);
    }

    /**
     * Calculates the total number of QR codes a user has scanned and updates the totalQRCodesScanned attribute.
     *
     */
    private void calculateTotalQRCodesScanned() {
        this.totalQRCodesScanned = this.qrCodes.size();
    }

    /**
     * Calculates the total sum of QR scores a user has scanned and updates the sumOfScoresScanned attribute.
     *
     */
    private void calculateSumOfScoresScanned() {
        Integer sum = 0;
        for (int i=0; i<(this.qrCodes.size()); i++) {
            sum += this.qrCodes.get(i).getQRScore();
        }
        this.sumOfScoresScanned = sum;
    }

    //TODO: Integrate firebase db to find rank
    /**
     * Calculates the a user's rank relative to all other users in database.
     *
     */
    private void calculateRank() {
        this.rank = -1;
    }

    /**
     * Updates a player's stats when a QR code has been added or removed.
     */
    private void updateStats() {
        this.calculateTotalQRCodesScanned();
        this.calculateSumOfScoresScanned();
        this.calculateRank();
    }
}

