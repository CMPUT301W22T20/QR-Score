package com.example.qrscore;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Purpose: This class is used to represent the profile fragment.
 * - It allows the user to view and edit their profile info.
 * - It allows the user to access their QR code.
 *
 * TODO: Implement view for QR code.
 *
 * Outstanding Issues:
 *
 *  @author William Liu
 */
public class ProfileFragment extends Fragment {

    private ProfileController profileController;
    private Profile profile;
    private TextView usernameTextView;
    private TextInputLayout firstNameLayout;
    private TextInputEditText firstNameTextEdit;
    private TextInputLayout lastNameLayout;
    private TextInputEditText lastNameTextEdit;
    private TextInputLayout emailLayout;
    private TextInputEditText emailTextEdit;
    private TextInputLayout phoneNumberLayout;
    private TextInputEditText phoneNumberEdit;
    private Button saveButton;

    /**
     * Purpose: A constructor for the profile fragment.
     */
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileController = new ProfileController(getContext());
        profileController.addProfileListener();
        profile = profileController.getProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        saveButton = view.findViewById(R.id.profile_save_button);
        saveButton.setOnClickListener(new ButtonOnClickListener());
        Toolbar toolbar = view.findViewById(R.id.profile_actionbar);
        toolbar.setTitle("");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        populateProfile(profile, view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileController.removeProfileListener();
    }

    /**
     * Purpose: Populate the profile view fragment with the players profile info.
     *
     * @param profile
     *      Represents a players profile.
     * @param view
     *      Represents a view that is contained within the fragment.
     */
    public void populateProfile(Profile profile, View view) {
        usernameTextView = view.findViewById(R.id.userUID_textView);
        usernameTextView.setText(profile.getUserUID());

        firstNameLayout = view.findViewById(R.id.first_name_textInputLayout);
        firstNameTextEdit = view.findViewById(R.id.first_name_textInputEditText);
        if (profile.getFirstName() == null || profile.getFirstName() == "") {
            firstNameTextEdit.setText("");
        } else {
            firstNameTextEdit.setText(profile.getFirstName());
        }

        lastNameLayout = view.findViewById(R.id.last_name_textInputLayout);
        lastNameTextEdit = view.findViewById(R.id.last_name_textInputEditText);
        if (profile.getLastName() == null || profile.getLastName() == "") {
            lastNameTextEdit.setText("");
        } else {
            lastNameTextEdit.setText(profile.getLastName());
        }

        emailLayout = view.findViewById(R.id.email_textInputLayout);
        emailTextEdit = view.findViewById(R.id.email_textInputEditText);
        if (profile.getEmail() == null || profile.getEmail() == "") {
            emailTextEdit.setText("");
        } else {
            emailTextEdit.setText(profile.getEmail());
        }

        phoneNumberLayout = view.findViewById(R.id.phone_number_textInputLayout);
        phoneNumberEdit = view.findViewById(R.id.phone_number_textInputEditText);
        if (profile.getEmail() == null || profile.getEmail() == "") {
            phoneNumberEdit.setText("");
        } else {
            phoneNumberEdit.setText(profile.getPhoneNumber());
        }
    }

    /**
     * Purpose: A class that implements the onClick method of the save button in the profileFragment.
     * - Calls updateProfile in the ProfileController to update the profile info.
     */
    private class ButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String firstName = firstNameTextEdit.getText().toString().trim();
            String lastName = lastNameTextEdit.getText().toString().trim();
            String email = emailTextEdit.getText().toString().trim();
            String phoneNumber = phoneNumberEdit.getText().toString().trim();
            String userUID = usernameTextView.getText().toString();
            if (firstName == "") {
                firstName = null;
            }
            if (lastName == "") {
                lastName = null;
            }
            if (email == "") {
                email = null;
            }
            if (phoneNumber == "") {
                phoneNumber = null;
            }
            Profile updatedProfile = new Profile(firstName, lastName, email, phoneNumber, userUID);
            profileController.updateProfile(updatedProfile, getActivity());
        }
    }
}

