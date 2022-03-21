package com.example.qrscore.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrscore.R;
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
public class ProfileAuthActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private ProgressBar profileProgressBar;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_auth);

        firebaseAuth = FirebaseAuth.getInstance();
        profileProgressBar = findViewById(R.id.auth_progress_bar);
        loginTextView = findViewById(R.id.auth_text_view);

        // Check if user is signed-in (non-null)
        // Profile is null/need to sign in and register in firestore.
        // https://firebase.google.com/docs/auth/android/anonymous-auth
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            createUser();
        }
        else {
            goToMainActivity();
        }
    }

    /**
     * Purpose: Creates a new user on firebase auth and initializes a new document on the firestore "Profile" collection.
     */
    private void createUser() {
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = firebaseAuth.getCurrentUser();
                            // create user on firestore db.
                            ProfileController profileController = new ProfileController(getApplicationContext());
                            profileController.createNewUser();
                            goToMainActivity();
                        }
                        // Failed to create user
                        else {
                            profileProgressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileAuthActivity.this, "Failed to sign in. Please close the app and try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Purpose: Go to the MainActivity after finished authorizing user.
     */
    private void goToMainActivity() {
        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProfileAuthActivity.this, MainActivity.class));
        finish();
    }

}