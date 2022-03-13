package com.example.qrscore;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Purpose: RecyclerAdapter for Player leaderboard.
 *
 * Outstanding Issues:
 *
 * @author: William Liu
 */
public class LeaderboardPlayerRecyclerAdapter extends RecyclerView.Adapter<LeaderboardPlayerRecyclerAdapter.MyViewHolder>{

    ArrayList<Player> players;

    public LeaderboardPlayerRecyclerAdapter(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Purpose: ViewHolder of items inside the Adapter.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView rank;
        private TextView score;
        private TextView username;
        private ImageButton playerMenuButton;

        public MyViewHolder(final View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.leaderboard_item_rank);
            score = itemView.findViewById(R.id.leaderboard_item_score);
            username = itemView.findViewById(R.id.leaderboard_item_username);
            playerMenuButton = itemView.findViewById(R.id.leaderboard_item_button);
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

        Player player = players.get(position);
        holder.rank.setText(player.getScore());
        holder.score.setText(player.getScore());
        holder.username.setText(player.getUsername());
        holder.playerMenuButton.setOnClickListener(new MenuButtonOnClickListener());
    }

    /**
     * Purpose: Returns the item count of the ViewHolder.
     *
     * @return
     *      Represents the number of items on the ViewHolder.
     */
    @Override
    public int getItemCount() {
        return players.size();
    }

    @NonNull
    @Override
    public LeaderboardPlayerRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * Purpose: Implements OnClickListener for menu button on item.
     */
    // https://www.youtube.com/watch?v=s1fW7CpiB9c
    private class MenuButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(),  view);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
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
     * @param playersFiltered
     *      Represents the players that have been filtered out.
     */
    public void filterList(ArrayList<Player> playersFiltered) {
        players = playersFiltered;
        notifyDataSetChanged();
    }
}
