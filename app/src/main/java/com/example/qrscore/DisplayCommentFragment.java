package com.example.qrscore;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Purpose: This class creates a dialog fragment to display a string of comments
 *
 * Outstanding issues:
 */
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