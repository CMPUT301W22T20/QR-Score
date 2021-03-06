package com.example.qrscore.controller;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.example.qrscore.model.Geolocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

/**
 * Purpose: A controller that deals with the location recorded.
 *
 * Outstanding Issues:
 *
 */
public class LocationController {
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Activity activity;
    private Location currLocation;
    private FirebaseFirestore db;
    private CollectionReference locationRef;
    private static double lat;
    private static double lon;

    public LocationController(Activity activity) {
        this.activity = activity;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        db = FirebaseFirestore.getInstance();
        locationRef = db.collection("Location");


        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currLocation = location;
                            //System.out.println(location.getLatitude());
                        }
                    }
                });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    currLocation = location;
                    lat = currLocation.getLatitude();
                    lon = currLocation.getLongitude();

                }
            }
        };
    }

    /**
     * Purpose: Get the latitude of the user.
     *
     * @return
     *      An instance of latitude double.
     */
    public double getLatitude() {
        return lat;
    }

    /**
     * Purpose: Get the longitude of the user.
     *
     * @return
     *      An instance of longitude double.
     */
    public double getLongitude() {
        return lon;
    }

    /**
     * Purpose: Save the location of a user.
     *
     * @param qrID
     *      The hash of the QR Code.
     * @param UUID
     *      Users unique UID.
     */
    public void saveLocation(String qrID, String UUID) {
        if (currLocation != null) {
            locationRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot docs = task.getResult();
                        if (!docs.isEmpty()) {
                            double currLat = currLocation.getLatitude();
                            double currLon = currLocation.getLongitude();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                GeoPoint geoPoint = (GeoPoint) doc.get("geoPoint");
                                double savedLat = geoPoint.getLatitude();
                                double savedLon = geoPoint.getLongitude();
                                double diffLat = Math.abs(currLat - savedLat);
                                double diffLon = Math.abs(currLon - savedLon);

                                // Check if a saved location is already close to the current one
                                if (diffLat < 0.0025 && diffLon < 0.0025) {
                                    // Saved location is already close to current

                                    ArrayList<String> uuids = (ArrayList<String>) doc.get("uuids");
                                    ArrayList<String> qrIDs = (ArrayList<String>) doc.get("qrIDs");

                                    // Update the array of qrIDs with the new one
                                    if (!qrIDs.contains(qrID)) {
                                        doc.getReference().update("qrIDs", FieldValue.arrayUnion(qrID));
                                    }

                                    // Update the array of UUIDs with new one
                                    if (!uuids.contains(UUID)) {
                                        // Person hasn't scanned it before
                                        doc.getReference().update("uuids", FieldValue.arrayUnion(UUID));
                                    }
                                    return;
                                }
                            }
                        }
                        // Add a new location to the database
                        ArrayList<String> UUIDs = new ArrayList<>();
                        UUIDs.add(UUID);
                        locationRef.add(new Geolocation(new GeoPoint(currLocation.getLatitude(), currLocation.getLongitude()), qrID, UUIDs));
                    }
                }
            });
        }
    }

    /**
     * Purpose: Start recording the location of user,
     */
    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        createLocationRequest();
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    /**
     * Purpose: Stop recording users location.
     */
    public void stopLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    /**
     * Purpose: Request location requests every 10 seconds.
     */
    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
}

