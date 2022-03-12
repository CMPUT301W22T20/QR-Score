package com.example.qrscore.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qrscore.Photo;
import com.example.qrscore.QRCode;
import com.example.qrscore.R;
import com.example.qrscore.controller.LocationController;
import com.example.qrscore.controller.PhotoController;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.nio.charset.StandardCharsets;

public class ScanFragment extends Fragment {
    private ImageView imageView;
    private LocationController locationController;
    private PhotoController photoController;
    private static Boolean fineLocationGranted;
    private static Boolean coarseLocationGranted;
    private String longitude;
    private String latitude;
    private FirebaseFirestore db;

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        photoController = new PhotoController();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        requestLocationPermissions();
        requestCameraPermissions();

        imageView = view.findViewById(R.id.qr_image_view);
        Button cameraButton = view.findViewById(R.id.button_take_photo);
        Button scanButton = view.findViewById(R.id.button_scan);

        // Start activity for scanning QR code
        final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                // Guava library hashing utilities
                final String hashed = Hashing.sha256()
                        .hashString(result.getContents(), StandardCharsets.UTF_8)
                        .toString();

                // Try to get document from db, if cant get then create a new one
                // so we don't overwrite existing data
                CollectionReference collectionReference = db.collection("QRCode");
                DocumentReference documentReference = collectionReference.document(hashed);
                if (!documentReference.get().isSuccessful()) {
                    documentReference.set(new QRCode(hashed, longitude, latitude));
                }
            }
        });

        scanButton.setOnClickListener(scanClicked -> {
            ScanOptions options = new ScanOptions();
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
            options.setPrompt("Scan a barcode");
            options.setBarcodeImageEnabled(true);
            barcodeLauncher.launch(options);
        });

        cameraButton.setOnClickListener(cameraClicked -> {
            requestCameraPermissions();
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            try {
                startActivityForResult(intent, 8008);
            } catch (SecurityException e) {
                Toast.makeText(getActivity(), "Camera Permissions Denied", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 8008 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Photo photo = new Photo(imageUri);
            photoController.uploadPhoto(photo);
            photoController.downloadPhoto();
            Toast.makeText(getActivity(), "Uploading Photo", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (locationController != null) {
            locationController.startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (locationController != null) {
            locationController.stopLocationUpdates();
        }
    }

    private void requestLocationPermissions() {
        // Requesting location permissions
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    fineLocationGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    coarseLocationGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);

                    if (fineLocationGranted != null && fineLocationGranted) {
                        // Precise location access granted.
                        // Start getting location updates
                        locationController = new LocationController(getActivity());
                        Toast.makeText(getActivity(), "Recording location", Toast.LENGTH_LONG).show();
                    }
                    else {
                        // Location not granted
                        Toast.makeText(getActivity(), "Not recording location", Toast.LENGTH_LONG).show();
                    }
                });
            locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            });
    }

    private void requestCameraPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 1001);
        }
    }
}
