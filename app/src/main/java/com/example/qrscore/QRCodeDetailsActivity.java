/* Purpose: This class represents the QR Code Details Activity.
Shows the location of the QRCode and players that have scanned it.
Shows players that have commented on the QRCode and allows user to click on the players to see their comments.
Allow user to add comments from this screen.

Outstanding issues:
 */

package com.example.qrscore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class QRCodeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_details);
    }
}