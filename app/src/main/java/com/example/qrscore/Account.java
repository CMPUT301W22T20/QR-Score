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
    private List<QRCode> qrCodes;
    private String device;
    private Profile profile;
    // private Stats stats;
    // private Permissions permissions;

    /**
     * Returns the array of QR codes.
     *
     * @return
     *      a List of QR codes.
     */
    public List<QRCode> getQRCodes() {
        return qrCodes;
    }

    /**
     * Adds a QR code to the list.
     *
     * @param toAdd
     *      the QR code to add.
     */
    public void addQRCode(QRCode toAdd) {
        qrCodes.add(toAdd);
    }

    /**
     * Removes a QR code from the list if it exists.
     *
     * @param toRemove
     *      the QR code to remove.
     */
    public void removeQRCode(QRCode toRemove) {
        // Check if the QR code is in the list or not, throw exception if it isn't.
        qrCodes.remove(toRemove);
    }

//    public QRCode getHighest() {
//        return stats.getHighscore(qrCodes);
//    }

//    public QRCode getLowest() {
//        return stats.getLowscore(qrCodes);
//    }
}
