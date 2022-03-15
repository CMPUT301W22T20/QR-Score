package com.example.qrscore;

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

    private String id;   // firestore document ID
    private String hash;
    private Integer qrscore;
    private String location;
    private List<String> hasScanned;
    private ArrayList<Comment> comments;

    /**
     * Constructor for the QR code.
     *
     * @param hash
     *      a String identifier for the QR code.
     */



    public QRCode(String hash) {
        Random random = new Random();
        this.hash = hash;
        this.qrscore = this.calculateQRScore(this.hash);
        this.id = this.qrscore.toString();
//        this.location = loc;
        this.comments  = new ArrayList<>();
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
    public QRCode(String hash, Integer qrscore) {
        this.hash = hash;
        this.qrscore = qrscore;
    }

    /**
     * Calculates QR Score from hash
     *
     * @param hash
     *      a String identifier for the QR code.
     */
    public Integer calculateQRScore(String hash) {
        return hash.length() - hash.replace("0", "").length();
    }

    /**
     * Get QR Score from member
     *
     */
    public Integer getQRScore() {
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

    /**
     * Removes a player from the list.
     *
     * @param player
     *      the id of the player to remove.
     */
    public void deleteScanned(String player) {
        for (int i=0; i<hasScanned.size(); i++) {
            if (hasScanned.get(i) == player) {
                hasScanned.remove(player);
            }
        }
    }

    /**
     * Checks if a player is in the list.
     *
     * @param player
     *      the player to find.
     * @return
     *      true if the player is in the list, false otherwise.
     */
    public boolean findScanned(Player player) {
        return hasScanned.contains(player);
    }

    /**
     * Adds a comment to the list.
     *
     * @param comment
     *      the comment to add.
     */
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    /**
     * Removes a comment from the list.
     *
     * @param comment
     *      the comment to remove.
     */
    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }

    /**
     * Checks if a comment is in the list.
     *
     * @param comment
     *      the comment to find.
     * @return
     *      true if the comment is in the list, false otherwise.
     */
    public boolean findComment(Comment comment) {
        return comments.contains(comment);
    }

    /**
     * Returns the list of comments.
     *
     * return
     *      an ArrayList of Strings.
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    public String getHash() {
        return hash;
    }

//    public String getLongitude() {
//        return longitude;
//    }
//
//    public String getLatitude() {
//        return latitude;
//    }
    /**
     * Constructor for QRCode
     *
     * @param hasScanned
     *      list of players that have scanned the QRCode
     */
    public QRCode(List<String> hasScanned) {
        this.hasScanned = hasScanned;
    }


    /**
     * Gets the players that have scanned the QRCode
     *
     * @return
     *      An ArrayList of players who have scanned the QR Code
     */
    public List<String> getHasScanned() { return hasScanned; }

    /**
     *  Gets the id of the QR Code
     *
     * @return
     *      The id of the qr code
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id for the QR Code
     *
     * @param id
     *      The id that is being set
     */
    public void setId(String id) {
        this.id = id;
    }


}
