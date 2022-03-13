package com.example.qrscore;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ProfileTest {
    @Test
    public void testCreateEmptyProfile() {
        Profile profile = new Profile();
        assertEquals(null, profile.getFirstName());
        assertEquals(null, profile.getLastName());
        assertEquals(null, profile.getEmail());
        assertEquals(null, profile.getPhoneNumber());
        assertEquals(null, profile.getUserUID());
    }

    @Test
    public void testCreateUserUIDProfile() {
        Profile profile = new Profile("cmput301w22t20");
        assertEquals(null, profile.getFirstName());
        assertEquals(null, profile.getLastName());
        assertEquals(null, profile.getEmail());
        assertEquals(null, profile.getPhoneNumber());
        assertEquals("cmput301w22t20", profile.getUserUID());
    }

    @Test
    public void testCreateProfile() {
        Profile profile = new Profile("CMPUT", "three-O'one", "player@qrscore.io", "7803012022", "cmput301w22t20");
        assertEquals("CMPUT", profile.getFirstName());
        assertEquals("three-O'one", profile.getLastName());
        assertEquals("player@qrscore.io", profile.getEmail());
        assertEquals("7803012022", profile.getPhoneNumber());
        assertEquals("cmput301w22t20", profile.getUserUID());
    }
}
