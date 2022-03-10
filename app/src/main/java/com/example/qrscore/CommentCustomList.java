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
 * This is a class that is a custom list to display various info
 */
public class CommentCustomList extends ArrayAdapter<Comment> {
    private ArrayList<Comment> comments;
    private Context context;

    public CommentCustomList(Context context, ArrayList<Comment> comments) {
        super(context, 0 , comments);
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.comment_content, parent, false);
        }

        Comment comment = comments.get(position);

        TextView dateText = view.findViewById(R.id.date_text_view);
        TextView playerText = view.findViewById(R.id.comment_player_text_view);

        dateText.setText(comment.getDate());
        playerText.setText(comment.getCommenter());

        return view;
    }
}