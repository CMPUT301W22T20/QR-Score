package com.example.qrscore;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

        Player player = players.get(position);
        holder.rank.setText("fd");
        holder.score.setText("fd");
        holder.name.setText("Fdsafdsfdfsdfsdfdsf");
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
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
                    Intent intent = new Intent(view.getContext(), OtherPlayerAccountActivity.class);
                    intent.putExtra("userID", "008pIplmeCdA35SkXKh2B2fL0B82");
                    view.getContext().startActivity(intent);
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
