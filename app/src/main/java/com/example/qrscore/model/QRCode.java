package com.example.qrscore.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Purpose: This class represents a QR code.
 * Stores a representation of the QR code, as well
 * as the score, location, players that have scanned it,
 * and comments.
 *
 * Outstanding issues:
 * TODO: Unit tests
 */
public class QRCode {
    String TAG = "QRCode";
    private String id;   // firestore document ID
    private String hash;
    private String qrscore;
    private List<String> hasScanned;

    /**
     * Constructor for the QR code.
     *
     * @param hash
     *      a String identifier for the QR code.
     */
    public QRCode(String hash) {
        this.hash = hash;
        Log.i(TAG, "new QRCode() with hash: " + this.hash);
        this.qrscore = this.calculateQRScore(this.hash);
        this.id = this.qrscore.toString();
        this.hasScanned = new ArrayList<>();
    }

    /**
     * Empty constructor for firebase
     */
    public QRCode() {}

    /**
     * Constructor required to display qr codes a player owns
     * @param hash
     *      The QRCode hash
     * @param qrscore
     *      The QRCode score
     */
    public QRCode(String hash, String qrscore) {
        this.hash = hash;
        this.qrscore = qrscore;
    }

    /**
     * Calculates QR Score from hash
     *
     * @param hash
     *      a String identifier for the QR code.
     */
    public String calculateQRScore(String hash) {
        String hash5 = hash.replace("00000", "");
        String hash4 = hash5.replace("0000", "");
        String hash3 = hash4.replace("000", "");
        String hash2 = hash3.replace("00", "");
        Integer count5 = (hash.length() - hash5.length())/5;
        Integer count4 = (hash.length() - hash4.length())/4;
        Integer count3 = (hash.length() - hash3.length())/3;
        Integer count2 = (hash.length() - hash2.length())/2;
        Integer score = count2*20 + count3*400 + count4*8000 + count5*160000;

        return score.toString();
    }

    /**
     * Get QR Score from member
     *
     */
    public String getQRScore() {
        return this.qrscore;
    }

    /**
     * Adds a player to the list.
     *
     * @param playerUsername
     *      the player to add.
     */
    public void addScanned(String playerUsername) {
        hasScanned.add(playerUsername);
    }

    public String getHash() {
        return hash;
    }

    /**
     *  Gets the id of the QR Code
     *
     * @return
     *      The id of the qr code
     */
    public String getId() {
        return id;
    }

    public List<String> getScanned() {
        return hasScanned;
    }
}
