package com.example.qrscore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Purpose: Custom adapter for QRCodes. Used to display QRCodes of a player
 *
 * Outstanding issues:
 */

public class QRCodeAdapter extends ArrayAdapter<QRCode> {

    private static final String TAG = "QRCodeAdapter";
    private Context mContext;
    int mResource;

    // Constructor for adapter
    public QRCodeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<QRCode> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get information
        Integer score = getItem(position).getQRScore();
        String hash = getItem(position).getHash();

        QRCode code = new QRCode(hash, score);   // create new QRCode object

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        // get textviews
        TextView score_text = (TextView) convertView.findViewById(R.id.qr_codes_score_text_view);
        TextView hash_text = (TextView) convertView.findViewById(R.id.qr_codes_id_text_view);

        // set textviews
        score_text.setText(score.toString());

        return convertView;
    }

}
