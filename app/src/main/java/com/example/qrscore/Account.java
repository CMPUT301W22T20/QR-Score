package com.example.qrscore;

import java.util.ArrayList;
import java.util.List;

/**
 * Purpose: This class represents an account in the system.
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
 */
public class Account {
    private List<String> devices;
    public QRDataList qrDataList;
    public Profile profile;
    // private Permissions permissions;

    public Account(String device, String userName) {
        this.devices = new ArrayList<String>();
        this.addDevice(device);
        this.profile = new Profile(userName);
        this.qrDataList = new QRDataList();
    }

    private void addDevice(String toAdd) {
        this.devices.add(toAdd);
    }
}