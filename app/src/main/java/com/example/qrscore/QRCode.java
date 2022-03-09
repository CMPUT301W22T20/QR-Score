package com.example.qrscore;

import java.util.ArrayList;
import java.util.List;

public class QRCode {
    private String id;
    private String codeRepr;
    private Score score;
    private String location;
    private List<String> hasScanned;
    private ArrayList<String> comments;

    public QRCode(String hash, String loc) {
        this.codeRepr = hash;
        // this.score = new Score(hash);
        this.location = loc;
        this.comments  = new ArrayList<>();
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
     * Empty constructor for firebase
     */
    public QRCode() {
    }


    /**
     * Gets the players that have scanned the QRCode
     *
     * @return
     *      An ArrayList of players who have scanned the QR Code
     */
    public List<String> getHasScanned() {

        return hasScanned;
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

    /**
     * Sets the id for the QR Code
     *
     * @param id
     *      The id that is being set
     */
    public void setId(String id) {
        this.id = id;
    }

    public void addToHasScanned(Player playerToAdd) {
        this.hasScanned = hasScanned;
    }
}
