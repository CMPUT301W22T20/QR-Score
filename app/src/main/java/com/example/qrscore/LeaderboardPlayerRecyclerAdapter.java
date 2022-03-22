package com.example.qrscore;

import android.app.Activity;
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

import com.example.qrscore.activity.OtherPlayerAccountActivity;
import com.example.qrscore.activity.QRCodeActivity;
import com.example.qrscore.fragment.OwnerLoginFragment;

import java.util.ArrayList;

/**
 * Purpose: RecyclerAdapter for Player leaderboard.
 *
 * Outstanding Issues:
 *
 * @author: William Liu
 */
public class LeaderboardPlayerRecyclerAdapter extends RecyclerView.Adapter<LeaderboardPlayerRecyclerAdapter.MyViewHolder> implements OwnerLoginFragment.OnFragmentInteractionListener {

    private ArrayList<Account> accounts;

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
        holder.rank.setText("NIL");
        holder.score.setText(account.getScore().toString());
        holder.name.setText(account.getUserID());
        holder.playerMenuButton.setOnClickListener(new MenuButtonOnClickListener(account));
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
        Account account;

        public MenuButtonOnClickListener(Account account) {
            this.account = account;
            this.userUID = account.getUserID();
        }

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
                        // delete player if account is owner
                        if (account.isOwner()) {
                            deletePlayer(userUID);
                        // display message, cannot delete
                        } else {
                            Toast.makeText(view.getContext(), "Only owners can delete players.",
                                    Toast.LENGTH_SHORT).show(); }
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
     * Purpose: sets Owner status to True for a specific user
     *
     * @param userUID
     *      The user to set the status true for
     */
    @Override
    public void onOwnerConfirmed(String userUID) {

    }
}
