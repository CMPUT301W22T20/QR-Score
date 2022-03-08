package com.example.qrscore;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

public class AccountTest {
    // Test ideas:
    //  1) Add a new account to database (unique ID), save that ID
    //  2) Add QR codes to database
    //  3) Get their IDs and link them to account in database
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference accountReference = db.collection("Account");

    @Test
    public void createNewAccount() {
        //a
    }

    @Test
    public void getExistingAccount() {
        //a
    }

    @Test
    public void testAddQR() {
        //a
    }

    @Test
    public void testRemoveQR() {
        //a
    }
}
