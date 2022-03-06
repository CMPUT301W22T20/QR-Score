package com.example.qrscore;

import java.util.List;

// TODO: As a player, I want to add new QR codes to my account.
// TODO: As a player, I want to see what QR codes I have added to my account.
// TODO: As a player, I want to remove QR codes from my account.
// TODO: As a player, I want to see my highest and lowest scoring QR codes.

/* Purpose: This class represents an account in the system.
 * Stores the QR codes tied to the account, the device,
 * and the profile.
 *
 * Outstanding issues:
 */
public class Account {
    private String device;
    private Profile profile;
    private Stats stats;
    // private Permissions permissions;

//    public QRCode getHighest() {
//        return stats.getHighscore(qrCodes);
//    }

//    public QRCode getLowest() {
//        return stats.getLowscore(qrCodes);
//    }
}
