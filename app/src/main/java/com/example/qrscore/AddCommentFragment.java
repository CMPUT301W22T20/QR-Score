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

import com.example.qrscore.Comment;
import com.example.qrscore.R;

/**
 * Purpose: This class creates a dialog fragment for the user to input a string into
 * Displays EditText for user to write a comment
 * Has "OK" and "Cancel" button
 * Posts comment when OK pressed
 *
 * Outstanding issues:
 * TODO: Connect to firebase
 * TODO: UI tests
 */
public class AddCommentFragment extends DialogFragment {
    private EditText commentText;
    private OnFragmentInteractionListener listener;
    private ProfileController profileController;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Comment newComment);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_comment, null);
        profileController = new ProfileController(getContext());

        commentText = view.findViewById(R.id.comment_editText);

        String qrID = getArguments().getString("QR_ID_TO_FRAGMENT");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Comment")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newComment = commentText.getText().toString();
                        // TODO: Replace Player1 with profile player name when possible
                        listener.onOkPressed(new Comment(profileController.getProfile().getFirstName(), newComment, qrID));
                    }
                }).create();
    }
}
