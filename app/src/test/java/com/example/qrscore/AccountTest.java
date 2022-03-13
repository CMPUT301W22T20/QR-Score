package com.example.qrscore;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Random;

public class AccountTest {
    // Test ideas:
    //  1) Add a new account to database (unique ID), save that ID
    //  2) Add QR codes to database
    //  3) Get their IDs and link them to account in database

    @Before
    public void initDB() {
        return;
    }

    @Test
    public void createNewAccount() {
        Random random = new Random();
        // From Baeldung
        // https://www.baeldung.com/java-random-string
        // https://www.baeldung.com/author/eugen/
        String key = random.ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(20)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        Account testAccount = new Account(key, "Pixel 2", "test_user");
        assertNotNull(testAccount);
        assertEquals(key, testAccount.getUserID());
        assertEquals("Pixel 2", testAccount.getDevice());
    }

//    @Test
//    public void getExistingAccount() {
//        //a
//    }
//
//    @Test
//    public void testAddQR() {
//        //a
//    }
//
//    @Test
//    public void testRemoveQR() {
//        //a
//    }
}
