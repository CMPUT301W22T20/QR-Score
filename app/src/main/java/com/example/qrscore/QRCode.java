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
}
