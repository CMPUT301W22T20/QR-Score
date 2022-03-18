package com.example.qrscore.fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.qrscore.R;
import com.example.qrscore.controller.LocationController;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

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

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        Button centerButton = view.findViewById(R.id.button_center);

        map = (MapView) view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(18.5);
        double lat = locationController.getLatitude();
        double lon = locationController.getLongitude();
        GeoPoint startPoint = new GeoPoint(lat, lon);
        mapController.setCenter(startPoint);

        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), map);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);

        centerButton.setOnClickListener(centerClicked -> {
            GeoPoint myPos = new GeoPoint(locationController.getLatitude(), locationController.getLongitude());
            mapController.animateTo(myPos, 18.5, (long) 1000);
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        locationController.stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();

        locationController.startLocationUpdates();
    }
}