package com.example.qrscore;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.hash.Hashing;
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
    private CustomLocation locationFragment;
    private static Boolean fineLocationGranted;
    private static Boolean coarseLocationGranted;

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
                                locationFragment = new CustomLocation(getActivity());
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
                    }
                    else {
                        // Guava library hashing utilities
                        final String hashed = Hashing.sha256()
                                .hashString(result.getContents(), StandardCharsets.UTF_8)
                                .toString();

                        longitudeText.setText(hashed);
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
        /* TODO: Crashes when location services denied but thats because we set it like that.
                 Will not crash when it is implemented properly.
        */
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location result = locationFragment.getLocation();

                String longitude = String.valueOf(result.getLongitude());
                longitudeText.setText(longitude);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (locationFragment != null) {
            locationFragment.startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (locationFragment != null) {
            locationFragment.stopLocationUpdates();
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 8008 && resultCode == Activity.RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(photo);
//        }
//    }
}