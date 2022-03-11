package com.example.qrscore;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private Profile newProfile;
    private Profile savedProfile;
    private String userUID;
    private Boolean updated;

    /**
     * Purpose: Constructor for a profileController that contains the firebase user and their document in the db.
     */
    public ProfileController() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        this.userUID = currentUser.getUid();
    }

    /**
     * Purpose: To create a new profile locally and on firestore db.
     *
     */
    public void createNewUser() {
        newProfile = new Profile(userUID);
        // https://firebase.google.com/docs/firestore/manage-data/add-data
        profileRef = db.collection("Profile").document(userUID);
        profileRef
                .set(newProfile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG", "Profile Created");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Profile Not Created :(", e);
                    }
                });
    }

    /**
     * Purpose: Returns the profileDocumentReference from firestore db.
     * @return
     *      An instance of the profileReference.
     */
    public DocumentReference getProfileRef() {
        profileRef = db.collection("Profile").document(userUID);
        return profileRef;
    }

    /**
     * Purpose: Updates the current players profile on firestore db.
     * @param updatedProfile
     *      An instance of their updated profile.
     * @param context
     *      ProfileFragment activity to display toast message.
     */
    public void updateProfile(Profile updatedProfile, Context context) {
        // https://firebase.google.com/docs/firestore/manage-data/add-data
        profileRef = db.collection("Profile").document(userUID);
        Map<String, Object> profile = new HashMap<>();
        profile.put("firstName", updatedProfile.getFirstName());
        profile.put("lastName", updatedProfile.getLastName());
        profile.put("email", updatedProfile.getEmail());
        profile.put("phoneNumber", updatedProfile.getPhoneNumber());

        // Update profile of user.
        profileRef
                .update(profile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "Profile successfully updated!");
                        Toast.makeText(context, "Profile has been updated!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Profile update unsuccessful :(", e);
                        Toast.makeText(context, "Profile has not been updated. Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }); }
}

