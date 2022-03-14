package com.example.qrscore;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

/**
 * Purpose:
 *
 * Outstanding issues:
 */

public class Geolocation {
    private GeoPoint geoPoint;
    private String qrID;
    private ArrayList<String> UUIDs;

    public Geolocation (GeoPoint geoPoint, String qrID, ArrayList<String> UUIDs) {
        this.geoPoint = geoPoint;
        this. qrID = qrID;
        this.UUIDs = UUIDs;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public String getQrID() {
        return qrID;
    }

    public void setQrID(String qrID) {
        this.qrID = qrID;
    }

    public ArrayList<String> getUUIDs() {
        return UUIDs;
    }

}
