package com.example.qrscore;

import java.util.List;

// TODO: As a player, I want to add new QR codes to my account.
// TODO: As a player, I want to see what QR codes I have added to my account.
// TODO: As a player, I want to remove QR codes from my account.
// TODO: As a player, I want to see my highest and lowest scoring QR codes.
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
