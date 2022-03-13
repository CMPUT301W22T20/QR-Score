/* Purpose: Custom list for Players who have scanned a QR code

Outstanding issues:
 */

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

public class PlayerCustomList extends ArrayAdapter<Player> {
    private ArrayList<Player> players;
    private Context context;

    public PlayerCustomList(Context context, ArrayList<Player> players) {
        super(context, 0 , players);
        this.players = players;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        // get the view
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.scanned_by_content, parent, false);
        }

        // set text to player username
        Player player = players.get(position);
        TextView playerText = view.findViewById(R.id.scanned_by_text_view);
//        playerText.setText(player.getUsername());

        return view;
    }
}
