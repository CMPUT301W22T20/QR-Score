package com.example.qrscore;

import java.util.ArrayList;
import java.util.List;

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
    private Integer qrScore;
    private String location;
    private List<String> hasScanned;
    private ArrayList<Comment> comments;

    /**
     * Constructor for the QR code.
     *
     * @param hash
     *      a String identifier for the QR code.
     * @param longitude
     *      a String representing the location of the QR code.
     */
    public QRCode(String hash, String longitude, String latitude) {
        this.hash = hash;
        // this.score = new Score(hash);
        this.location = loc;
        this.comments  = new ArrayList<>();
        calculateQRScore(this.hash);
    }

    /**
     * Calculates QR Score from hash
     *
     * @param hash
     *      a String identifier for the QR code.
     */
    public void calculateQRScore(String hash) {
        Integer score;
        //TODO
        score = 1;
        this.qrScore = score;
    }

    /**
     * Get QR Score from member
     *
     */
    public Integer getQRScore() {
        return this.qrScore;
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
     *      the player to remove.
     */
    public void deleteScanned(Player player) {
        hasScanned.remove(player);
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

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }
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
