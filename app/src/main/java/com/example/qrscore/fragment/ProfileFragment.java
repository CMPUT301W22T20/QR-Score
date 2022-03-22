package com.example.qrscore.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrscore.Profile;
import com.example.qrscore.QRGeneratorDialog;
import com.example.qrscore.R;
import com.example.qrscore.controller.ProfileController;
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
    protected Profile profile;
    private TextInputLayout userUIDLayout;
    private TextInputEditText userUIDTextEdit;
    private TextInputLayout firstNameLayout;
    private TextInputEditText firstNameTextEdit;
    private TextInputLayout lastNameLayout;
    private TextInputEditText lastNameTextEdit;
    private TextInputLayout emailLayout;
    private TextInputEditText emailTextEdit;
    private TextInputLayout phoneNumberLayout;
    private TextInputEditText phoneNumberEdit;
    private Button saveButton;
    private Button generateQRButton;


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
        Toolbar toolbar = view.findViewById(R.id.profile_actionbar);
        saveButton = view.findViewById(R.id.profile_save_button);
        generateQRButton = view.findViewById(R.id.profile_generateQR_button);
        toolbar.setTitle("");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        populateProfile(profile, view);

        phoneNumberEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        saveButton.setOnClickListener(new SaveButtonOnClickListener());
        emailTextEdit.addTextChangedListener(new EmailTextWatcher());
        emailTextEdit.setOnFocusChangeListener(new EmailFocusChangedListener());
        generateQRButton.setOnClickListener(new QRCodeButtonOnClickListener());
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
    private void populateProfile(Profile profile, View view) {

        userUIDLayout = view.findViewById(R.id.userUID_textInputLayout);
        userUIDTextEdit = view.findViewById(R.id.userUID_textInputEditText);
        userUIDTextEdit.setText(profile.getUserUID());
        userUIDTextEdit.setInputType(InputType.TYPE_NULL);
        userUIDTextEdit.setFocusable(false);

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

        emailTextEdit = view.findViewById(R.id.email_textInputEditText);
        emailLayout = view.findViewById(R.id.email_textInputLayout);
        if (profile.getEmail() == null || profile.getEmail() == "") {
            emailTextEdit.setText("");
            emailLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
        } else {
            emailTextEdit.setText(profile.getEmail());
            emailTextEdit.setInputType(InputType.TYPE_NULL);
        }

        phoneNumberLayout = view.findViewById(R.id.phone_number_textInputLayout);
        phoneNumberEdit = view.findViewById(R.id.phone_number_textInputEditText);
        if (profile.getPhoneNumber() == null || profile.getPhoneNumber() == "") {
            phoneNumberEdit.setText("");
        } else {
            phoneNumberEdit.setText(profile.getPhoneNumber());
        }
    }

    /**
     * Purpose: A class that implements the onClick method of the save button in the profileFragment.
     * - Calls updateProfile in the ProfileController to update the profile info.
     */
    private class SaveButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String firstName = firstNameTextEdit.getText().toString().trim();
            String lastName = lastNameTextEdit.getText().toString().trim();
            String email = emailTextEdit.getText().toString();
            String phoneNumber = phoneNumberEdit.getText().toString().trim();
            String userUID = userUIDTextEdit.getText().toString();

            if (!isValidEmail(email) && !(email.isEmpty())) {
                Toast.makeText(getContext(), "Email Invalid, Profile not saved", Toast.LENGTH_SHORT).show();
            }
            else {
                email = email.trim();
                if (firstName.isEmpty()) {
                    firstName = null;
                }
                if (lastName.isEmpty()) {
                    lastName = null;
                }
                if (email.isEmpty()) {
                    email = null;
                } else {
                    emailTextEdit.setInputType(InputType.TYPE_NULL);
                    emailLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    emailLayout.setErrorEnabled(false);
                }
                if (phoneNumber.isEmpty()) {
                    phoneNumber = null;
                }
                Profile updatedProfile = new Profile(firstName, lastName, email, phoneNumber, userUID, profile.getPermanent());
                profileController.updateProfile(updatedProfile, getActivity());
                profile = profileController.getProfile();
            }
        }
    }

    private class EmailTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            emailLayout.setError("Email can only be edited once!");
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!isValidEmail(charSequence)) {
                emailLayout.setError("Invalid Email");
            }
            else {
                emailLayout.setError("Email can only be edited once!");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private class EmailFocusChangedListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean changed) {
            if (changed) {
                emailLayout.setError("Email can only be edited once!");
            }
        }
    }

    //https://stackoverflow.com/a/15808057
    private static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private class QRCodeButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ProfileController profileController = new ProfileController(getContext());
            profile = profileController.getProfile();
            if (profile.getPermanent()) {
                Toast.makeText(getContext(), "QR Generating", Toast.LENGTH_SHORT).show();
                openQRDialog();
            }
            else if((profile.getEmail() != null) && !(profile.getPermanent())) {
                profileController.convertAccount(getContext(), converted -> {
                    if (converted) {
                        Toast.makeText(getContext(), "QR Generating", Toast.LENGTH_SHORT).show();
                        profile = profileController.getProfile();
                        openQRDialog();
                    }
                    else {
                        Toast.makeText(getContext(), "Cannot Generate QR Code, Email already exists to another user!", Toast.LENGTH_LONG).show();
                    }
                });

            }
            else {
                Toast.makeText(getContext(), "Email Required for QR Code generation", Toast.LENGTH_SHORT).show();
            }
        }

        private void openQRDialog() {
            // https://stackoverflow.com/a/15459259
            Bundle args = new Bundle();
            args.putString("email", profile.getEmail());
            args.putString("userUID", profile.getUserUID());
            args.putBoolean("login", true);

            QRGeneratorDialog qrGeneratorDialog = new QRGeneratorDialog();
            qrGeneratorDialog.setArguments(args);
            qrGeneratorDialog.show(getChildFragmentManager(), "QR Dialog");
        }
    }
}

