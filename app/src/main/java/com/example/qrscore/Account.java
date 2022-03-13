package com.example.qrscore;

import java.util.ArrayList;
import java.util.List;

/* Purpose: This class represents an account in the system.
 * Stores the QR codes tied to the account, the device,
 * and the profile.
 *
 * Outstanding issues:
 * TODO: Finish Purpose
 * TODO: As a player, I want to add new QR codes to my account.
 * TODO: As a player, I want to see what QR codes I have added to my account.
 * TODO: As a player, I want to remove QR codes from my account.
 * TODO: As a player, I want to see my highest and lowest scoring QR codes.
 * TODO: Unit tests
 * TODO: UI--navbar with "add" button to add QR codes.
 */
public class Account {
    private static final String TAG = "ACCOUNT";
    private String userID;
    public Profile profile;
    private List<String> devices;
    public QRDataList qrDataList;
    // private Permissions permissions;

    public Account(String userID, String device, String userName) {
        this.userID = userID;
        this.devices = new ArrayList<String>();
        this.addDevice(device);
        this.qrDataList = new QRDataList();
        this.profile = new Profile(userName);
    }

    private void addDevice(String toAdd) {
        this.devices.add(toAdd);
    }

    /**
     * Returns the Account's user ID.
     *
     * @return
     *      user ID as a string.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Returns the Account's device.
     *
     * @return
     *      device as a string.
     */
    public String getDevice() {
        return devices.get(0);
    }

    /**
     * Adds a QR code to the list.
     *
     * @param qr
     *      the QR code to add.
     */
    public void addQR(QRCode qr) {
        qrDataList.addQRCode(qr);
    }

    /**
     * Returns the array of QR codes.
     *
     * @return
     *      a List of QR codes.
     */
    public List<QRCode> getQR() {
        return qrDataList.getQRCodes();
    }

    /**
     * Removes a QR code from the list if it exists.
     *
     * @param qr
     *      the QR code to remove.
     */
    public void removeQR(QRCode qr) {
        qrDataList.removeQRCode(qr);
    }

    public Integer getHighest() {
        return qrDataList.getHighscore();
    }

    public Integer getLowest() {
        return qrDataList.getLowscore();
    }
}
