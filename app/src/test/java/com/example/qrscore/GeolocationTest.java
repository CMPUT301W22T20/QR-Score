package com.example.qrscore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.qrscore.model.Geolocation;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.util.ArrayList;

public class GeolocationTest {
    ArrayList<String> uuidsList = new ArrayList<>();
    ArrayList<Geolocation> geolocationsList = new ArrayList<>();

    @Test
    public void testCreateGeolocation() {
        double lat = 57.245676;
        double lon = -153.562344;
        String qrId = "testQrID";
        String uuid = "testUuid";

        uuidsList.add(uuid);

        GeoPoint geoPoint = new GeoPoint(lat, lon);

        Geolocation geolocation = new Geolocation(geoPoint, qrId, uuidsList);

        assertEquals(0, geolocationsList.size());

        geolocationsList.add(geolocation);

        assertEquals(1, geolocationsList.size());
    }

    @Test
    public void testGetters() {
        double lat = 57.245676;
        double lon = -153.562344;
        String qrId = "testQrID";
        String uuid = "testUuid";

        uuidsList.add(uuid);

        GeoPoint geoPoint = new GeoPoint(lat, lon);

        Geolocation geolocation = new Geolocation(geoPoint, qrId, uuidsList);

        assertEquals(geoPoint, geolocation.getGeoPoint());

        assertEquals(qrId, geolocation.getQrID());

        assertEquals(uuidsList, geolocation.getUUIDs());

        String newUuid = geolocation.getUUIDs().get(0);

        assertEquals(uuid, newUuid);
    }
}
