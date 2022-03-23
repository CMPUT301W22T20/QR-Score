package com.example.qrscore.controller;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrscore.R;
import com.example.qrscore.model.QRCode;
import com.example.qrscore.activity.QRCodeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.ArrayList;

/**
 * Purpose:
 *
 * Outstanding issues:
 * TODO: Delete Comment
 *
 */
public class LeaderboardQRCodeRecyclerAdapter extends RecyclerView.Adapter<LeaderboardQRCodeRecyclerAdapter.MyViewHolder> {

    private ArrayList<QRCode> qrCodes;
    private FirebaseFirestore db;
    private boolean isOwner;
    private FirebaseFunctions mFunctions;
    private FirebaseAuth firebaseAuth;

    public LeaderboardQRCodeRecyclerAdapter(ArrayList<QRCode> qrCodes) {
        this.qrCodes = qrCodes;
    }

    /**
     * Purpose: ViewHolder of items inside the Adapter.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView rank;
        private TextView score;
        private TextView name;
        private TextView qrCodeTitle;
        private ImageButton playerMenuButton;

        public MyViewHolder(final View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.list_item_rank);
            score = itemView.findViewById(R.id.list_item_score);
            name = itemView.findViewById(R.id.list_item_name);
            qrCodeTitle = itemView.findViewById(R.id.list_item_player_textView);
            playerMenuButton = itemView.findViewById(R.id.list_item_menu_button);
        }
    }

    /**
     * Purpose: Set items on ViewHolder.
     * @param holder
     *      Represents a ViewHolder.
     *
     * @param position
     *      Represents the position on the ViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull LeaderboardQRCodeRecyclerAdapter.MyViewHolder holder, int position) {
        QRCode qrCode = qrCodes.get(position);
        holder.rank.setText("NIL");
        holder.score.setText("NIL");
        holder.name.setText(qrCode.getHash());
        holder.playerMenuButton.setOnClickListener(new LeaderboardQRCodeRecyclerAdapter.MenuButtonOnClickListener(qrCode.getHash()));
        holder.qrCodeTitle.setText("QR Code");
    }


    /**
     * Purpose: Returns the item count of the ViewHolder.
     *
     * @return
     *      Represents the number of items on the ViewHolder.
     */
    @Override
    public int getItemCount() {
        return qrCodes.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public LeaderboardQRCodeRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }


    /**
     * Purpose: Implements OnClickListener for menu button on item.
     */
    // https://www.youtube.com/watch?v=s1fW7CpiB9c
    private class MenuButtonOnClickListener implements View.OnClickListener {

        String hash;

        public MenuButtonOnClickListener(String hash) {this.hash = hash; }

        @Override
        public void onClick(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(),  view);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    // start QRCodeActivity if view qrCode is clicked
                    if (menuItem.getItemId() == R.id.leaderboard_qrcode_item) {
                        Intent intent = new Intent(view.getContext(), QRCodeActivity.class);
                        intent.putExtra("QR_ID", hash);
                        view.getContext().startActivity(intent);
                        return true;

                        // if delete player is clicked
                    } else if (menuItem.getItemId() == R.id.delete_qrcode_item) {

                        db = FirebaseFirestore.getInstance();   // initialize db

                        // check if user is owner
                        if (userIsOwner()) {
                            try {
                                deleteCode(hash);
                            } catch (FirebaseAuthException e) {
                                e.printStackTrace();
                            }

                            // display message, cannot delete
                        } else {
                            Toast.makeText(view.getContext(), "Only owners can delete QR Codes.",
                                    Toast.LENGTH_SHORT).show(); }
                        return true;
                    }
                    return false;
                }
            });
            popupMenu.inflate(R.menu.leaderboard_qrcode_menu);
            popupMenu.show();
        }
    }


    /**
     * Purpose: Deletes a code from the app
     *
     * @param hash
     *      The hash of the code to delete
     */
    public void deleteCode(String hash) throws FirebaseAuthException {
        firebaseAuth = FirebaseAuth.getInstance();

        // delete qrcode
        db.collection("QRCode").document(hash).delete();

        // query qrcode documents that user has scanned
        db.collection("Account").whereArrayContains("QRCodes", hash).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        // get each individual QRCode document
                        for (DocumentSnapshot accountDocument : querySnapshot.getDocuments()) {

                            // remove the player from QRCode scanned array
                            if (accountDocument.exists()) {
                                accountDocument.getReference().update("scanned", FieldValue.arrayRemove(hash));
                            }
                        }
                    }
                });

        // query location that QRCode has
        db.collection("Location").whereArrayContains("qrIDs", hash).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        // get each individual Location document
                        for (DocumentSnapshot locationDocument : querySnapshot.getDocuments()) {

                            // remove the QRCode from Location qr codes
                            if (locationDocument.exists()) {
                                locationDocument.getReference().update("qrIDs", FieldValue.arrayRemove(hash));
                            }
                        }
                    }
                });
        notifyDataSetChanged();
    }

    /**
     * Purpose Checks if user is owner
     *
     * @return
     *      True if user is owner, false otherwise.
     */
    public boolean userIsOwner() {

        firebaseAuth = FirebaseAuth.getInstance();
        String userUID = firebaseAuth.getCurrentUser().getUid();

        // get account document
        db.collection("Account").document(userUID).get()
                .addOnCompleteListener(taskAccount -> {
                    if (taskAccount.isSuccessful()) {
                        DocumentSnapshot accountDocument = taskAccount.getResult();

                        if (accountDocument.exists()) {
                            if (accountDocument.getBoolean("isOwner") != null) {
                                isOwner = accountDocument.getBoolean("isOwner");
                            }
                        }
                    }
                });
        return isOwner;
    }
}

