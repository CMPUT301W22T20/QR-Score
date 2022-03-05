package com.example.qrscore;

import java.util.ArrayList;

public class QRCode {
    private String codeRepr;
    private Score score;
    private String location;
    private ArrayList<Player> hasScanned;
    private ArrayList<String> comments;

    public QRCode(String hash, String loc) {
        this.codeRepr = hash;
        // this.score = new Score(hash);
        this.location = loc;
        this.hasScanned = new ArrayList<>();
        this.comments  = new ArrayList<>();
    }

    public void addScanned(Player player) {
        hasScanned.add(player);
    }

    public void deleteScanned(Player player) {
        hasScanned.remove(player);
    }

    public boolean findScanned(Player player) {
        return hasScanned.contains(player);
    }

    public ArrayList<Player> getScanned() {
        return hasScanned;
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public void deleteComment(String comment) {
        comments.remove(comment);
    }

    public boolean findComment(String comment) {
        return comments.contains(comment);
    }

    public ArrayList<String> getComments() {
        return comments;
    }
}
