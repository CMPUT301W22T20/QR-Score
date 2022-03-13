package com.example.qrscore;

import com.google.firebase.firestore.GeoPoint;

public class Geolocation {
    private GeoPoint geoPoint;
    private String qrID;
    private String uUID;

    public Geolocation (GeoPoint geoPoint, String qrID, String uUID) {
        this.geoPoint = geoPoint;
        this. qrID = qrID;
        this .uUID = uUID;
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

    public String getuUID() {
        return uUID;
    }

    public void setuUID(String uUID) {
        this.uUID = uUID;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}
