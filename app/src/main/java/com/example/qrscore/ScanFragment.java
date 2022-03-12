package com.example.qrscore;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.nio.charset.StandardCharsets;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView imageView;
    private LocationController locationController;
    private static Boolean fineLocationGranted;
    private static Boolean coarseLocationGranted;
    private String longitude;
    private String latitude;
    private FirebaseFirestore db;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance(String param1, String param2) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        db = FirebaseFirestore.getInstance();

        this.requestPermissions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        imageView = view.findViewById(R.id.qr_image_view);
        Button cameraButton = view.findViewById(R.id.button_take_photo);
        TextView latitudeText = view.findViewById(R.id.latitude_text_view);
        TextView longitudeText = view.findViewById(R.id.longitude_text_view);
        Button scanButton = view.findViewById(R.id.button_scan);

        // Start activity for scanning QR code
        final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
                result -> {
                    if (result.getContents() == null) {
                        Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
                    } else {
                        // Guava library hashing utilities
                        final String hashed = Hashing.sha256()
                                .hashString(result.getContents(), StandardCharsets.UTF_8)
                                .toString();

                        longitudeText.setText(hashed);

                        // Try to get document from db, if cant get then create a new one
                        // so we don't overwrite existing data
                        CollectionReference collectionReference = db.collection("QRCode");
                        collectionReference.document(hashed)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d("Sample", "DocumentSnapshot data: " + document.getData());
                                            } else {
                                                Log.d("Sample", "No such document");
                                                longitude = String.valueOf(locationController.getLocation().getLongitude());
                                                latitude = String.valueOf(locationController.getLocation().getLatitude());
                                                collectionReference.document(hashed).set(new QRCode(hashed, longitude, latitude));
                                            }
                                        } else {
                                            Log.d("Sample", "get failed with ", task.getException());
                                        }
                                    }
                                });
                    }
                });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivityForResult(intent, 8008);
                ScanOptions options = new ScanOptions();
                options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
                options.setPrompt("Scan a barcode");
                options.setBarcodeImageEnabled(true);
                barcodeLauncher.launch(options);
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Location result = locationController.getLocation();
                    String longitude = String.valueOf(result.getLongitude());
                    longitudeText.setText(longitude);
                }
                catch (NullPointerException e) {
                    Toast.makeText(getActivity(), "An Error Occurred", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
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

    private void requestPermissions() {
        // Requesting location permissions
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                        .RequestMultiplePermissions(), result -> {
                            fineLocationGranted = result.get(
                                    Manifest.permission.ACCESS_FINE_LOCATION);
                            coarseLocationGranted = result.get(
                                    Manifest.permission.ACCESS_COARSE_LOCATION);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                                // Start getting location updates
                                locationController = new LocationController(getActivity());
                                Toast.makeText(getActivity(), "Recording location", Toast.LENGTH_LONG).show();
                                System.out.println("1");
                            }
                            else {
                                // Location not granted
                                Toast.makeText(getActivity(), "Not recording location", Toast.LENGTH_LONG).show();
                                System.out.println("3");
                            }
                        }
                );
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 8008 && resultCode == Activity.RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(photo);
//        }
//    }
}