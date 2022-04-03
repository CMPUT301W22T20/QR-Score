package com.example.qrscore.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.qrscore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Purpose: Prompts user for a password to confirm that the user is an owner.
 * Has OK and Cancel buttons. When OK is pressed and password is correct, userUID is added
 * as an owner and can delete players and QRCodes.
 *
 * Outstanding Issues:
 */
public class OwnerLoginFragment extends DialogFragment {
    private EditText password_edit_text;
    private OnFragmentInteractionListener confirmOwnerListener;
    private Button showHideButton;
    private FirebaseAuth mAuth;
    private Context mContext;

    public interface OnFragmentInteractionListener {
        void onOwnerConfirmed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mContext = context;
            confirmOwnerListener = (OnFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    // Dialog is the window that pops up when the button is clicked
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Make view owner_login_fragment_layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_owner_login_layout, null);

        // Get password from editText box
        password_edit_text = view.findViewById(R.id.password_edit_text);

        // Show/Hide button for password: https://www.tutorialkart.com/kotlin-android/android-show-hide-password-in-edittext/
        showHideButton = view.findViewById(R.id.showHideButton);
        showHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Show password
                if(showHideButton.getText().toString().equals("Show")){
                    password_edit_text.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showHideButton.setText("Hide");

                // Hide password
                } else {
                    password_edit_text.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showHideButton.setText("Show");
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Enter Owner Credentials")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override

                    // Check if sign in is successful
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String pass = password_edit_text.getText().toString();
                        if (pass.equals("password")) {
                            confirmOwnerListener.onOwnerConfirmed();
                        } else {Toast.makeText(mContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create(); }
}