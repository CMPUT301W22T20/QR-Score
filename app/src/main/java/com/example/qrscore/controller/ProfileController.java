package com.example.qrscore.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qrscore.model.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

/**
 * Purpose: This class is used to control a profile.
 *  - Creating new users
 *  - Getting the profile reference from firestore db
 *  - Updating the profile in firestore db
 *
 *  Outstanding Issues:
 *
 *  @author William Liu
 */
public class ProfileController {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private DocumentReference profileRef;
    private AccountController accountController;
    private Profile newProfile;
    private String userUID;
    private ListenerRegistration profileListener;
    private SharedPreferences profileSP;
    private SharedPreferences.Editor profileSPEditor;
    private String email;
    private Boolean permanent;

    private static final String PROFILE_PREFS = "profilePrefs";
    private static final String TAG = "Profile";

    /**
     * Purpose: Constructor for a profileController that contains the firebase user and their document in the db.
     */
    public ProfileController(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        // https://www.youtube.com/watch?v=fJEFZ6EOM9o
        profileSP = context.getSharedPreferences(PROFILE_PREFS, Context.MODE_PRIVATE);
        profileSPEditor = profileSP.edit();
    }

    /**
     * Purpose: To create a new profile locally and on firestore db.
     * To create a new account on firestore db.
     */
    public void createNewUser(String userUID) {
        newProfile = new Profile(userUID);
        // https://firebase.google.com/docs/firestore/manage-data/add-data
        profileRef = db.collection("Profile").document(userUID);
        profileRef
                .set(newProfile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "Profile Created");
                        setProfile(newProfile);
                        accountController = new AccountController();
                        accountController.createNewAccount();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Profile Not Created :(", e);
                    }
                });
    }

    /**
     * Purpose: Add a profileListener for firestore data.
     */
    public void addProfileListener() {
        userUID = currentUser.getUid();
        profileRef = db.collection("Profile").document(userUID);
        profileListener = profileRef.addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                Profile savedProfile = snapshot.toObject(Profile.class);
                setProfile(savedProfile);    // Update profile locally
                Log.d(TAG, savedProfile.getUserUID() + " profile snapshot exists!");
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
    }

    /**
     * Purpose: Remove profileListener when player leaves ProfileFragment.
     */
    public void removeProfileListener() {
        profileListener.remove();
    }

    /**
     * Purpose: Updates the current players profile on firestore db and locally.
     *
     * @param updatedProfile An instance of their updated profile.
     * @param context        ProfileFragment activity to display toast message.
     */

    // Profile Callback
    public void updateProfile(Profile updatedProfile, Context context) {
        // https://firebase.google.com/docs/firestore/manage-data/add-data
        userUID = currentUser.getUid();
        profileRef = db.collection("Profile").document(userUID);
        Map<String, Object> profile = new HashMap<>();
        profile.put("firstName", updatedProfile.getFirstName());
        profile.put("lastName", updatedProfile.getLastName());
        profile.put("email", updatedProfile.getEmail());
        email = updatedProfile.getEmail();
        permanent = updatedProfile.getPermanent();
        profile.put("phoneNumber", updatedProfile.getPhoneNumber());
        profile.put("permanent", updatedProfile.getPermanent());

        // Update profile of user.
        profileRef
                .update(profile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Profile successfully updated!");
                        Toast.makeText(context, "Profile has been updated!", Toast.LENGTH_SHORT).show();
                        setProfile(updatedProfile);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Profile update unsuccessful :(", e);
                        Toast.makeText(context, "Profile has not been updated. Please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Purpose: Set/Update profile info in SharedPrefs.
     *
     * @param newProfile Profile to be set/updated with locally.
     */
    public void setProfile(Profile newProfile) {
        profileSPEditor.putString("firstName", newProfile.getFirstName());
        profileSPEditor.putString("lastName", newProfile.getLastName());
        profileSPEditor.putString("email", newProfile.getEmail());
        profileSPEditor.putString("phoneNumber", newProfile.getPhoneNumber());
        profileSPEditor.putString("userUID", newProfile.getUserUID());
        profileSPEditor.putBoolean("permanent", newProfile.getPermanent());
        profileSPEditor.apply();
    }

    /**
     * Purpose: Return an instance of the profile saved locally
     *
     * @return Represents the Profile object locally.
     */
    public Profile getProfile() {
        String firstName = profileSP.getString("firstName", null);
        String lastName = profileSP.getString("lastName", null);
        String email = profileSP.getString("email", null);
        String phoneNumber = profileSP.getString("phoneNumber", null);
        String userUID = profileSP.getString("userUID", currentUser.getUid());
        Boolean permanent = profileSP.getBoolean("permanent", false);
        Profile profile = new Profile(firstName, lastName, email, phoneNumber, userUID, permanent);
        return profile;
    }

    public void convertAccount(Context context, ConvertAccountCallback convertAccountCallback) {
        Profile profile = getProfile();
        AuthCredential credential = EmailAuthProvider.getCredential(profile.getEmail(), profile.getUserUID());
        firebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            profile.setPermanent(true);
                            updateProfile(profile, context);
                            convertAccountCallback.onCallback(true);
                            Log.d(TAG, "linkWithCredential:success");
                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            convertAccountCallback.onCallback(false);
                        }
                    }
                });
    }
}


