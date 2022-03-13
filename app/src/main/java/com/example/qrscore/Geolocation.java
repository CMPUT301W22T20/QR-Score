package com.example.qrscore;

public class Geolocation {
    private double lat;
    private double lon;

    public Geolocation (double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
