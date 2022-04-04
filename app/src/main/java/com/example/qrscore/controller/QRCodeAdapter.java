package com.example.qrscore.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.qrscore.R;
import com.example.qrscore.activity.QRCodeActivity;
import com.example.qrscore.model.QRCode;
import java.util.ArrayList;

/**
 * Purpose: Custom adapter for QRCodes. Used to display QRCodes of a player
 *
 * Outstanding issues:
 * TODO: set rank
 */

public class QRCodeAdapter extends ArrayAdapter<QRCode> {

    private static final String TAG = "QRCodeAdapter";
    private Context mContext;
    private int mResource;
    private LayoutInflater inflater;
    private String score;
    private String hash;
    private QRCode code;
    private TextView score_text;
    private TextView hash_text;
    private TextView rank_text;
    private TextView qrCode_text;

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
        score = getItem(position).getQRScore();
        hash = getItem(position).getHash();

        code = new QRCode(hash, score);   // create new QRCode object

        inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        // get textviews
        score_text = (TextView) convertView.findViewById(R.id.list_item_score);
        rank_text = (TextView) convertView.findViewById(R.id.list_item_rank);
        hash_text = (TextView) convertView.findViewById(R.id.list_item_name);
        qrCode_text = (TextView) convertView.findViewById(R.id.list_item_player_textView);

        // set textviews
        qrCode_text.setText("QR Code");
        score_text.setText(score.toString());
        hash_text.setText(hash);

        // TODO: set rank

        final ImageButton button = convertView.findViewById(R.id.list_item_menu_button);

        // Show popup menu to view QRCode when clicked on
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(mContext, button);
                popup.getMenuInflater().inflate(R.menu.view_qrcode_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        // start QRCode Activity
                        if (item.getItemId() == R.id.view_qrcode_item) {
                            Intent intent = new Intent(mContext, QRCodeActivity.class);
                            intent.putExtra("QR_ID", hash);
                            mContext.startActivity(intent);
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                });
                popup.show();
            }
        });

        return convertView;
    }


}
