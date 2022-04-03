package com.example.qrscore.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrscore.R;
import com.example.qrscore.controller.AccountController;
import com.example.qrscore.controller.ProfileController;
import com.example.qrscore.fragment.ScanFragmentPlayer;
import com.example.qrscore.fragment.OwnerLoginFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import java.util.ArrayList;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * Purpose: This class is used to authorize a user on firebase auth.
 *
 *  Outstanding Issues:
 *
 *  @author William Liu
 */
public class ProfileAuthActivity extends AppCompatActivity implements View.OnClickListener, OwnerLoginFragment.OnFragmentInteractionListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private AppCompatButton returningButton;
    private AppCompatButton loginByQRButton;
    private AppCompatButton newUserButton;
    private AppCompatButton ownerButton;
    private ProgressBar profileProgressBar;
    private TextView loginTextView;

    private ActivityResultLauncher<ScanOptions> barcodeLauncher;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    private String email;
    private String userUID;
    private boolean blocked;


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
        barcodeLauncher = startActivityCamera();

        requestPermissionsIfNecessary(new String[] {
                // if you need to show the current location, uncomment the line below
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        ownerButton = findViewById(R.id.auth_owner_button);
        ownerButton.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
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
                else if (userIsBlocked(currentUser.getUid())) {
                    Toast.makeText(this, "Cannot login, you have been banned", Toast.LENGTH_LONG).show();
                }
                else {
                    loginTextView.setVisibility(View.VISIBLE);
                    profileProgressBar.setVisibility(View.VISIBLE);
                    goToMainActivity();
                }
                break;
            case R.id.auth_login_by_qr_user_button:
                currentUser = firebaseAuth.getCurrentUser();
                ScanOptions options = new ScanOptions();
                options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
                options.setPrompt("Scan a QR Code");
                barcodeLauncher.launch(options);

                //signInWithEmail();
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
            // if user is an owner
            case R.id.auth_owner_button:
                currentUser = firebaseAuth.getCurrentUser();

                // Create user if they don't have account
                if (currentUser == null) {
                    loginTextView.setVisibility(View.VISIBLE);
                    profileProgressBar.setVisibility(View.VISIBLE);
                    createNewUser();
                    // Show owner login fragment
                    new OwnerLoginFragment().show(getSupportFragmentManager(), "OWNER LOGIN");

                } else {
                    userUID = currentUser.getUid();
                    if (userIsBlocked(currentUser.getUid())) {
                        Toast.makeText(this, "Cannot login, you have been banned", Toast.LENGTH_LONG).show();
                    } else {
                        // Show owner login fragment
                        new OwnerLoginFragment().show(getSupportFragmentManager(), "OWNER LOGIN");
                    }
                }
                break;
        }
    }

    private ActivityResultLauncher<ScanOptions> startActivityCamera() {
        final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                // Get the contents from the qr code
                String contents = result.getContents();

                // Check if its one of our qr codes
                if (contents.contains(",")) {
                    // Split the string from the comma separator
                    String[] res = contents.split("[,]", 0);

                    if (res.length == 2) {
                        String email = res[0];
                        String userUID = res[1];

                        signInWithEmail(email, userUID);
                    }
                }
                else {
                    // Scanned QR Code is invalid
                    Toast.makeText(this, "Invalid QR", Toast.LENGTH_LONG).show();
                }            }
        });

        return barcodeLauncher;
    }

    private void signInWithEmail(String email, String userUID) {
        loginTextView.setVisibility(View.VISIBLE);
        profileProgressBar.setVisibility(View.VISIBLE);
        //email = currentUser.getEmail();
        firebaseAuth.signInWithEmailAndPassword(email, userUID)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        currentUser = firebaseAuth.getCurrentUser();
                        return;
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
     * Returns true if user is blocked
     *
     * @param userUID
     *      userUID of user to check
     * @return boolean
     *      true if user is blocked, false otherwise
     */
    public boolean userIsBlocked(String userUID) {

        blocked = false;

         DocumentReference docRef = db.collection("BlockedUsers").document(userUID);

         docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        blocked = true;
                    }
                } else {
                    Log.d("ProfileAuthActivity", "get failed with ", task.getException());
                }
            }
        });
         return blocked;
    }

    /**
     * Purpose: Go to the MainActivity after finished authorizing user.
     */
    private void goToMainActivity() {
        startActivity(new Intent(ProfileAuthActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Purpose: sets Owner status to True for a specific user
     */
    @Override
    public void onOwnerConfirmed() {

        userUID = currentUser.getUid();
        db.collection("Account").document(userUID).update("isOwner", "true");   // update owner status
        goToMainActivity();
    }

}