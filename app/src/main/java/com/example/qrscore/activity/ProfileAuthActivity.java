package com.example.qrscore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrscore.R;
import com.example.qrscore.controller.AccountController;
import com.example.qrscore.controller.ProfileController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Purpose: This class is used to authorize a user on firebase auth.
 *
 *  Outstanding Issues:
 *
 *  @author William Liu
 */
public class ProfileAuthActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private AppCompatButton returningButton;
    private AppCompatButton loginByQRButton;
    private AppCompatButton newUserButton;
    private ProgressBar profileProgressBar;
    private TextView loginTextView;
    private String email;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_auth);

        firebaseAuth = FirebaseAuth.getInstance();
        profileProgressBar = findViewById(R.id.auth_progress_bar);
        loginTextView = findViewById(R.id.auth_login_text_view);
        returningButton = findViewById(R.id.auth_returning_user_button);
        returningButton.setOnClickListener(this);
        loginByQRButton = findViewById(R.id.auth_login_by_qr_user_button);
        loginByQRButton.setOnClickListener(this);
        newUserButton = findViewById(R.id.auth_new_user_button);
        newUserButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.auth_returning_user_button:
                currentUser = firebaseAuth.getCurrentUser();
                // Check if user is signed-in (non-null)
                // Profile is null/need to sign in and register in firestore.
                // https://firebase.google.com/docs/auth/android/anonymous-auth
                if (currentUser == null) {
                    Toast.makeText(this, "Cannot locate user, Please sign up as a NEW USER!", Toast.LENGTH_LONG).show();
                }
                else {
                    loginTextView.setVisibility(View.VISIBLE);
                    profileProgressBar.setVisibility(View.VISIBLE);
                    goToMainActivity();
                }
                break;
            case R.id.auth_login_by_qr_user_button:
                currentUser = firebaseAuth.getCurrentUser();
                signInWithEmail();
                break;
            case R.id.auth_new_user_button:
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser == null) {
                    loginTextView.setVisibility(View.VISIBLE);
                    profileProgressBar.setVisibility(View.VISIBLE);
                    createNewUser();
                }
                else {
                    Toast.makeText(this, "User already created. Please select RETURNING!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void signInWithEmail() {
        loginTextView.setVisibility(View.VISIBLE);
        profileProgressBar.setVisibility(View.VISIBLE);
        email = currentUser.getEmail();
        firebaseAuth.signInWithEmailAndPassword(email, userUID)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        currentUser = firebaseAuth.getCurrentUser();
                        goToMainActivity();
                    }
                    else {
                        profileProgressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileAuthActivity.this, "Cannot sign in. Please close the app and try again!", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    /**
     * Purpose: Creates a new user on firebase auth and initializes a new document on the firestore "Profile" collection.
     */
    private void createNewUser() {
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        currentUser = firebaseAuth.getCurrentUser();
                        // create profile on firestore db.
                        ProfileController profileController = new ProfileController(getApplicationContext());
                        profileController.createNewProfile(currentUser.getUid());
                        // create account on firestore db.
                        AccountController accountController = new AccountController(getApplicationContext());
                        accountController.createNewAccount(currentUser.getUid());
                        goToMainActivity();
                    }
                    // Failed to create user
                    else {
                        profileProgressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileAuthActivity.this, "Failed to sign in. Please close the app and try again!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Purpose: Go to the MainActivity after finished authorizing user.
     */
    private void goToMainActivity() {
        startActivity(new Intent(ProfileAuthActivity.this, MainActivity.class));
        finish();
    }

}