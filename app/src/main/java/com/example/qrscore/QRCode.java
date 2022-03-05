package com.example.qrscore;

import java.util.ArrayList;

/* Purpose: This class represents a QR code.
 * Stores a representation of the QR code, as well
 * as the score, location, players that have scanned it,
 * and comments.
 *
 * Outstanding issues:
 */
public class QRCode {
    private String codeRepr;
    private Integer score;
    private String location;
    private ArrayList<Player> hasScanned;
    private ArrayList<Comment> comments;

    /**
     * Constructor for the QR code.
     *
     * @param hash
     *      a String identifier for the QR code.
     * @param loc
     *      a String representing the location of the QR code.
     */
    public QRCode(String hash, String loc) {
        this.codeRepr = hash;
        // this.score = new Score(hash);
        this.location = loc;
        this.hasScanned = new ArrayList<>();
        this.comments  = new ArrayList<>();
    }

    /**
     * Adds a player to the list.
     *
     * @param player
     *      the player to add.
     */
    public void addScanned(Player player) {
        hasScanned.add(player);
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
     * Returns the list of players who have scanned the QR code.
     *
     * return
     *      an ArrayList of players.
     */
    public ArrayList<Player> getScanned() {
        return hasScanned;
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
}
