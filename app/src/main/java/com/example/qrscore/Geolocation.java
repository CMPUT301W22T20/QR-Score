package com.example.qrscore;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

/**
 * Purpose: Represents a Geolocation.
 *
 * Outstanding issues:
 *
 */

public class Geolocation {
    private GeoPoint geoPoint;
    private ArrayList<String> qrIDs;
    private ArrayList<String> UUIDs;

    public Geolocation (GeoPoint geoPoint, String qrID, ArrayList<String> UUIDs) {
        this.geoPoint = geoPoint;
        this.qrIDs = new ArrayList<>();
        this.qrIDs.add(qrID);
        this.UUIDs = UUIDs;
    }

    /**
     * Purpose: Get the geoPoint of current instance.
     *
     * @return
     *      An instance of a GeoPoint.
     */
    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    /**
     * Purpose: Get the QR Code hash.
     *
     * @return
     *      The QR Code hash.
     */
    public ArrayList<String> getQrIDs() {
        return qrIDs;
    }

    /**
     * Purpose: Set the QR Code hash.
     *
     * @param qrID
     *      A QR Code hash.
     */
    public void addQrID(String qrID) {
        this.qrIDs.add(qrID);
    }

    /**
     * Purpose: Return UserUIDS of geolocation
     * @return
     *      An Arraylist of UserUID Strings.
     */
    public ArrayList<String> getUUIDs() {
        return UUIDs;
    }

}
