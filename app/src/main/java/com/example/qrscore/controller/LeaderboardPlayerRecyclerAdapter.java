package com.example.qrscore.controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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
import com.example.qrscore.activity.OtherPlayerAccountActivity;
import com.example.qrscore.activity.QRCodeActivity;
import com.example.qrscore.fragment.OwnerLoginFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.functions.FirebaseFunctions;
import com.example.qrscore.model.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Purpose: RecyclerAdapter for Player leaderboard.
 *
 * Outstanding Issues:
 *      TODO: add user id to comments to delete comment
 *      TODO: Have a list of blocked players that cant login
 *      TODO: Delete player from location
 *
 * @author: William Liu
 */
public class LeaderboardPlayerRecyclerAdapter extends RecyclerView.Adapter<LeaderboardPlayerRecyclerAdapter.MyViewHolder> {

    private ArrayList<Account> accounts;
    private FirebaseFirestore db;
    private boolean isOwner;
    private FirebaseAuth firebaseAuth;

    public LeaderboardPlayerRecyclerAdapter(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * Purpose: ViewHolder of items inside the Adapter.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView rank;
        private TextView score;
        private TextView name;
        private ImageButton playerMenuButton;

        public MyViewHolder(final View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.list_item_rank);
            score = itemView.findViewById(R.id.list_item_score);
            name = itemView.findViewById(R.id.list_item_name);
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
    public void onBindViewHolder(@NonNull LeaderboardPlayerRecyclerAdapter.MyViewHolder holder, int position) {
        Account account = accounts.get(position);
        holder.rank.setText(account.getRankTotalScore());
        holder.score.setText(account.getTotalScore());
        Log.d("Account score", account.getTotalScore());
        holder.name.setText(account.getUserUID());
        holder.playerMenuButton.setOnClickListener(new MenuButtonOnClickListener(account.getUserUID()));
    }

    /**
     * Purpose: Returns the item count of the ViewHolder.
     *
     * @return
     *      Represents the number of items on the ViewHolder.
     */
    @Override
    public int getItemCount() {
        return accounts.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public LeaderboardPlayerRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * Purpose: Implements OnClickListener for menu button on item.
     */
    // https://www.youtube.com/watch?v=s1fW7CpiB9c
    private class MenuButtonOnClickListener implements View.OnClickListener {

        String userUID;

        public MenuButtonOnClickListener(String userUID) {this.userUID = userUID; }

        @Override
        public void onClick(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(),  view);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    // start OtherPlayerAccountActivity if view profile is clicked
                    if (menuItem.getItemId() == R.id.leaderboard_profile_item) {
                        Intent intent = new Intent(view.getContext(), OtherPlayerAccountActivity.class);
                        intent.putExtra("userID", userUID);
                        view.getContext().startActivity(intent);
                        return true;

                    // if delete player is clicked
                    } else if (menuItem.getItemId() == R.id.delete_player_item) {

                        db = FirebaseFirestore.getInstance();   // initialize db

                        // check if user is owner
                        if (userIsOwner()) {
                            try {
                                deletePlayer(userUID);
                            } catch (FirebaseAuthException e) {
                                e.printStackTrace();
                            }

                        // display message, cannot delete
                        } else {
                            Toast.makeText(view.getContext(), "Only owners can delete players.",
                                    Toast.LENGTH_SHORT).show(); }
                        return true;
                    }
                    return false;
                }
            });
            popupMenu.inflate(R.menu.leaderboard_profile_menu);
            popupMenu.show();
        }
    }

    /**
     * Purpose: Update Adapter with filtered players when using the search function.
     *
     * @param accountsFiltered
     *      Represents the players that have been filtered out.
     */
    public void updateList(ArrayList<Account> accountsFiltered) {
        accounts = accountsFiltered;
        notifyDataSetChanged();
    }

    /**
     * Purpose: Deletes a player from the app
     *
     * @param userUID
     *      The user ID of the player to delete
     */
    public void deletePlayer(String userUID) throws FirebaseAuthException {
        firebaseAuth = FirebaseAuth.getInstance();

        // delete profile and account
        db.collection("Profile").document(userUID).delete();
        db.collection("Account").document(userUID).delete();

        // query qrcode documents that user has scanned
        db.collection("QRCode").whereArrayContains("scanned", userUID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        // get each individual QRCode document
                        for (DocumentSnapshot qrCodeDocument : querySnapshot.getDocuments()) {

                            // remove the player from QRCode scanned array
                            if (qrCodeDocument.exists()) {
                                qrCodeDocument.getReference().update("scanned", FieldValue.arrayRemove(userUID));
                            }
                        }
                        }
                    });

        // query location that player has
        db.collection("Location").whereArrayContains("uuids", userUID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        // get each individual Location document
                        for (DocumentSnapshot locationDocument : querySnapshot.getDocuments()) {

                            // remove the QRCode from Location qr codes
                            if (locationDocument.exists()) {
                                locationDocument.getReference().update("uuids", FieldValue.arrayRemove(userUID));
                            }
                        }
                    }
                });

        // block user
        DocumentReference blockedUserRef = db.collection("BlockedUsers").document(userUID);
        HashMap<String, Object> blockedUser = new HashMap<>();
        blockedUser.put("userUID", userUID);
        blockedUserRef.set(blockedUser);
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
                            if (accountDocument.getString("isOwner") != null) {
                                isOwner = Boolean.valueOf(accountDocument.getString("isOwner"));
                            }
                        }
                    }
                });
        return isOwner;
    }
}
