package com.example.qrscore.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.qrscore.activity.OtherPlayerAccountActivity;
import com.example.qrscore.R;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

/**
 * Purpose: To scan QR Codes and add a photo to the QR Codes.
 *
 * Outstanding Issues:
 *
 */
// TODO: Replace tempUUID with actual UUID
public class ScanFragmentPlayer extends Fragment {

    public ScanFragmentPlayer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        Button scanButton = view.findViewById(R.id.button_scan);
        SwitchCompat locationButton = view.findViewById(R.id.switch_button);

        // Removing the location button cause we don't need it for this fragment
        locationButton.setVisibility(View.GONE);

        // Start activity for scanning QR code
        final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                // Get the contents from the qr code
                String contents = result.getContents();

                // Check if its one of our qr codes
                if (contents.contains(",") && contents.split("[,]", 0).length == 1) {
                    // Split the string from the comma separator
                    String[] res = contents.split("[,]", 0);

                    // Trying to lookup someones stats QR Code
                    String userUID = res[0]; // redundant but just for clarity

                    Intent intent = new Intent(getContext(), OtherPlayerAccountActivity.class);
                    intent.putExtra("userID", userUID);
                    startActivity(intent);
                }
                else {
                    // Scanned QR Code is invalid
                    Toast.makeText(getActivity(), "Invalid QR", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Action for when the user clicks on the scan button
        scanButton.setOnClickListener(scanClicked -> {
            ScanOptions options = new ScanOptions();
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
            options.setPrompt("Scan a QR Code");
            barcodeLauncher.launch(options);
        });

        return view;
    }
}
