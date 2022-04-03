package com.example.qrscore;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRGeneratorDialog extends DialogFragment {

    ImageView qrCode;
    String email;
    String userUID;
    Boolean login;
    String account;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle mArgs = getArguments();
        email = mArgs.getString("email");
        userUID = mArgs.getString("userUID");
        login = mArgs.getBoolean("login");

        View view = inflater.inflate(R.layout.qr_generator_diaglog, container, false);

        qrCode = view.findViewById(R.id.qr_generator_image_view);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        generateQR();
        return view;
    }

    // https://www.youtube.com/watch?v=n8HdrLYL9DA
    private void generateQR() {
        MultiFormatWriter writer = new MultiFormatWriter();
        if (login) {
            account = email + "," + userUID;
        }
        else {
            account = userUID + ",";
        }

        try {
            BitMatrix bitMatrix = writer.encode(account, BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);
        }
        catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
