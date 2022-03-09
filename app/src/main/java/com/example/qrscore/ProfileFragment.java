package com.example.qrscore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ListenerRegistration;

// https://www.youtube.com/watch?v=IxhIa3eZxz8
// https://www.youtube.com/watch?v=LfkhFCDnkS0&list=PLrnPJCHvNZuDrSqu-dKdDi3Q6nM-VUyxD&index=4

public class ProfileFragment extends Fragment {

    Profile savedProfile;
    DocumentReference profileRef;
    ProfileController profileController;
    ListenerRegistration profileListener;
    TextView usernameTextView;
    TextInputLayout firstNameLayout;
    TextInputEditText firstNameTextEdit;
    TextInputLayout lastNameLayout;
    TextInputEditText lastNameTextEdit;
    TextInputLayout emailLayout;
    TextInputEditText emailTextEdit;
    TextInputLayout phoneNumberLayout;
    TextInputEditText phoneNumberEdit;
    Button saveButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileController = new ProfileController();
        profileRef = profileController.getProfileRef();
        profileListener = profileRef.addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                Profile savedProfile = snapshot.toObject(Profile.class);
                populateProfile(savedProfile, getView());
                Log.d("TAG", savedProfile.getUserUID());
            }
            else {
                Log.d("TAG", "Current data: null");
            }
        });
    }

    public void populateProfile(Profile profile, View view) {
        usernameTextView = view.findViewById(R.id.userUID_textView);
        usernameTextView.setText(profile.getUserUID());

        firstNameLayout = view.findViewById(R.id.first_name_textInputLayout);
        firstNameTextEdit = view.findViewById(R.id.first_name_textInputEditText);
        if (profile.getFirstName() == null || profile.getFirstName() == "") {
            firstNameTextEdit.setText("");
        }
        else {
            firstNameTextEdit.setText(profile.getFirstName());
        }

        lastNameLayout = view.findViewById(R.id.last_name_textInputLayout);
        lastNameTextEdit = view.findViewById(R.id.last_name_textInputEditText);
        if (profile.getLastName() == null || profile.getLastName() == "") {
            lastNameTextEdit.setText("");
        }
        else {
            lastNameTextEdit.setText(profile.getLastName());
        }

        emailLayout = view.findViewById(R.id.email_textInputLayout);
        emailTextEdit = view.findViewById(R.id.email_textInputEditText);
        if (profile.getEmail() == null || profile.getEmail() == "") {
            emailTextEdit.setText("");
        }
        else {
            emailTextEdit.setText(profile.getEmail());
        }

        phoneNumberLayout = view.findViewById(R.id.phone_number_textInputLayout);
        phoneNumberEdit = view.findViewById(R.id.phone_number_textInputEditText);
        if (profile.getEmail() == null || profile.getEmail() == "") {
            phoneNumberEdit.setText("");
        }
        else {
            phoneNumberEdit.setText(profile.getPhoneNumber());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        saveButton = view.findViewById(R.id.profile_save_button);
        saveButton.setOnClickListener(new ButtonOnClickListener());
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileListener.remove();
    }

    private class ButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String firstName = firstNameTextEdit.getText().toString().trim();
            String lastName = lastNameTextEdit.getText().toString().trim();
            String email = emailTextEdit.getText().toString().trim();
            String phoneNumber = phoneNumberEdit.getText().toString().trim();
            String userUID =  usernameTextView.getText().toString();
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
            profileController.updateProfile(updatedProfile);
        }
    }

}

