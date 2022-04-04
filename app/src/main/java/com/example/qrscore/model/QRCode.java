package com.example.qrscore.model;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Purpose: This class represents a QR code.
 * Stores a representation of the QR code, as well
 *
 */
public class QRCode {
    String TAG = "QRCode";
    private String id;   // firestore document ID
    private String hash;
    private String qrscore;
    private List<String> hasScanned;

    /**
     * Purpose: Constructor for the QR code.
     *
     * @param hash
     *      a String identifier for the QR code.
     */
    public QRCode(String hash) {
        this.hash = hash;
        this.qrscore = this.calculateQRScore(this.hash).toString();
        this.id = this.qrscore;
        this.hasScanned = new ArrayList<>();
    }

    /**
     * Purpose: Empty constructor for firebase
     */
    public QRCode() {}

    /**
     * Purpose: Constructor required to display qr codes a player owns
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
     * Purpose: Calculates QR Score from hash
     *
     * @param hash
     *      a String identifier for the QR code.
     */
    public String calculateQRScore(String hash) {
        String hash5 = hash.replace("00000", "");
        String hash4 = hash5.replace("0000", "");
        String hash3 = hash4.replace("000", "");
        String hash2 = hash3.replace("00", "");
        String hash1 = hash2.replace("0", "");
        int count5 = (hash.length() - hash5.length())/5;
        int count4 = (hash5.length() - hash4.length())/4;
        int count3 = (hash4.length() - hash3.length())/3;
        int count2 = (hash3.length() - hash2.length())/2;
        int count1 = (hash2.length() - hash1.length());

        return count1 + (count2 * 20) + (count3 * 400) + (count4 * 8000) + (count5 * 160000);
    }

    /**
     * Purpose: Get QR Score from member
     *
     */
    public String getQRScore() {
        return this.qrscore;
    }

    /**
     * Purpose: Adds a player to the list.
     *
     * @param playerUsername
     *      the player to add.
     */
    public void addToHasScanned(String playerUsername) {
        hasScanned.add(playerUsername);
    }

    public String getHash() {
        return hash;
    }

    /**
     * Purpose: Gets the id of the QR Code
     *
     * @return
     *      The id of the qr code
     */
    public String getId() {
        return id;
    }

    /**
     * Purpose: Get the list of userUIDs that have scanned this QR Code.
     *
     * @return
     *      ArrayList of userUIDs.
     */
    public List<String> getHasScanned() {
        return hasScanned;
    }
}
