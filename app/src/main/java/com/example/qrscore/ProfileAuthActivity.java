package com.example.qrscore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        // Profile is null
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            CreateUser();
        }
        else {
            finish();
        }
    }

    private void CreateUser() {
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            currentUser = firebaseAuth.getCurrentUser();
                            // create user.
                            // go to QRCodeActivity.
                            ProfileController profileController = new ProfileController();
                            profileController.createNewUser();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            // Not sure what should happen here.
                        }
                    }
                });
    }

}