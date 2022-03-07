package com.example.qrscore;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DisplayCommentFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        String commentText = args.getString("COMMENT");
        String playerText = args.getString("PLAYER_NAME");

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        return builder
                .setTitle(playerText)
                .setMessage(commentText)
                .setPositiveButton("DONE", null)
                .create();
    }
}