package com.example.qrscore.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.qrscore.R;
import com.example.qrscore.controller.LocationController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

// TODO: Implement the path for when user denies location services
/**
 * Purpose: This class is used to represent the map fragment.
 *
 * Outstanding Issues:
 *
 *  @author
 */
public class MapFragment extends Fragment {
    private MapView map = null;

    private LocationController locationController;

    private FirebaseFirestore db;
    private CollectionReference locationRef;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        locationRef = db.collection("Location");

        locationController = new LocationController(getActivity());
        locationController.startLocationUpdates();

        Context ctx = getContext().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_map, container, false);

        FloatingActionButton centerButton = view.findViewById(R.id.button_center);
        Button searchButton = view.findViewById(R.id.button_map_confirm);
        EditText latEdit = view.findViewById(R.id.map_lat_edit_text);
        EditText lonEdit = view.findViewById(R.id.map_lon_edit_text);
        TextView errorText = view.findViewById(R.id.map_error_text_view);

        // Initialize the map
        map = (MapView) view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        // Setting zoom and touch controls
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        // Setting the maps starting point at the player location if enabled
        IMapController mapController = map.getController();
        mapController.setZoom(18.5);
        double lat = locationController.getLatitude();
        double lon = locationController.getLongitude();
        GeoPoint startPoint = new GeoPoint(lat, lon);
        mapController.setCenter(startPoint);

        // Enabling the marker for the player's location
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), map);
        mLocationOverlay.enableMyLocation();
        Bitmap myLogo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_gps_fixed_24);
        mLocationOverlay.setPersonIcon(myLogo);
        map.getOverlays().add(mLocationOverlay);

        // Set qr location markers on the map
        locationRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> docs = task.getResult().getDocuments();

                for (DocumentSnapshot doc: docs) {
                    // Convert firebase's geopoint to osmdroid's geopoint
                    com.google.firebase.firestore.GeoPoint pos = (com.google.firebase.firestore.GeoPoint) doc.getData().get("geoPoint");
                    GeoPoint realPos = new GeoPoint(pos.getLatitude(), pos.getLongitude());

                    // Get a count of how many qrcodes are near this location
                    ArrayList<String> qrCodes = (ArrayList<String>) doc.getData().get("qrIDs");
                    int nearbyQRs = qrCodes.size();

                    // Create the marker and add it to the map
                    Marker qrMarker = new Marker(map);
                    qrMarker.setPosition(realPos);
                    qrMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                    qrMarker.setTitle("Nearby QRs: " + nearbyQRs);
                    map.getOverlays().add(qrMarker);
                }
            }
        });

        // Button to center map on players location
        centerButton.setOnClickListener(centerClicked -> {
            GeoPoint myPos = new GeoPoint(locationController.getLatitude(), locationController.getLongitude());
            mapController.animateTo(myPos, 18.5, (long) 1000);
        });

        // Button to search for location on the map
        searchButton.setOnClickListener(searchClicked -> {
            boolean validLocation = true;
            try {
                double latSearch = Double.parseDouble(latEdit.getText().toString());
                double lonSearch = Double.parseDouble(lonEdit.getText().toString());

                GeoPoint searchPoint = new GeoPoint(latSearch, lonSearch);
                mapController.animateTo(searchPoint, 18.5, (long) 1000);
            } catch (Exception e) {
                validLocation = false;
                errorText.setText("Invalid location!");
                e.printStackTrace();
            }

            if (validLocation) {
                errorText.setText("");
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        map.onPause();
        locationController.stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();

        map.onResume();
        locationController.startLocationUpdates();
    }
}