package com.example.qrscore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaderboardRecyclerAdapter extends RecyclerView.Adapter<LeaderboardRecyclerAdapter.MyViewHolder>{

    ArrayList<Player> players;

    public LeaderboardRecyclerAdapter(ArrayList<Player> players) {
        this.players = players;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView rank;
        private TextView score;
        private TextView username;
        private ImageButton playerMenu;

        public MyViewHolder(final View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.leaderboard_item_rank);
            score = itemView.findViewById(R.id.leaderboard_item_score);
            username = itemView.findViewById(R.id.leaderboard_item_username);
            playerMenu = itemView.findViewById(R.id.leaderboard_item_button);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardRecyclerAdapter.MyViewHolder holder, int position) {

        for (int i = 0; i < getItemCount(); i++) {

            String rank = "893";
            holder.rank.setText(rank);

//        String username = players.get(position).getUsername();
            String username = "Xm0Z8ZAEljVRPT9EGA2uphT86bh1";
            holder.username.setText(username);
//
//        String score = players.get(position).getScore();
            String score = "15000";
            holder.score.setText(score);
        }
//
//        holder.playerMenu.setOnClickListener(new MenuButtonOnClickListener());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    @NonNull
    @Override
    public LeaderboardRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    private class MenuButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

        }
    }

}
