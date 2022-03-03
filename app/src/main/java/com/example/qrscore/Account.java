package com.example.qrscore;

import java.util.List;

//  As a player, I want to see that other players have scanned the same QR code.
public class Account {
    private List<QRCode> qrCodes;
    private String device;
    private Profile profile;
    // private Stats stats;
    // private Permissions permissions;

    public List<QRCode> getQRCodes() {
        return qrCodes;
    }

    public void addQRCode(QRCode toAdd) {
        qrCodes.add(toAdd);
    }

    public void removeQRCode(QRCode toRemove) {
        qrCodes.remove(toRemove);
    }

//    public QRCode getHighest() {
//        return stats.getHighscore(qrCodes);
//    }

//    public QRCode getLowest() {
//        return stats.getLowscore(qrCodes);
//    }
}
