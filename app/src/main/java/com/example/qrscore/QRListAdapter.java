package com.example.qrscore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Purpose: Custom List adapter for a QR Code list.
 *
 * Outstanding Issues:
 *
 */
public class QRListAdapter extends ArrayAdapter<QRCode> {
    private ArrayList<QRCode> qrs;
    private Context context;

    public QRListAdapter(Context context, ArrayList<QRCode> qrs) {
        super(context, 0, qrs);
        this.qrs = qrs;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.qr_view, parent,false);
        }
        QRCode qr = qrs.get(position);
        TextView qrName = view.findViewById(R.id.qr_name_id);
        qrName.setText(qr.getId());

        return view;
    }
}
