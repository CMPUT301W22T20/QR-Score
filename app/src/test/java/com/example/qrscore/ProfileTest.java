package com.example.qrscore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.qrscore.model.Profile;

import org.junit.Test;

public class ProfileTest {
    @Test
    public void testCreateEmptyProfile() {
        Profile profile = new Profile();
        assertNull(profile.getFirstName());
        assertNull(profile.getLastName());
        assertNull(profile.getEmail());
        assertNull(profile.getPhoneNumber());
        assertNull(profile.getUserUID());
    }

    @Test
    public void testCreateUserUIDProfile() {
        Profile profile = new Profile("cmput301w22t20");
        assertNull(profile.getFirstName());
        assertNull(profile.getLastName());
        assertNull(profile.getEmail());
        assertNull(profile.getPhoneNumber());
        assertEquals("cmput301w22t20", profile.getUserUID());
    }

    @Test
    public void testCreateProfile() {
        Profile profile = new Profile("CMPUT", "three-O'one", "player@qrscore.io", "7803012022", "cmput301w22t20", false);
        assertEquals("CMPUT", profile.getFirstName());
        assertEquals("three-O'one", profile.getLastName());
        assertEquals("player@qrscore.io", profile.getEmail());
        assertEquals("7803012022", profile.getPhoneNumber());
        assertEquals("cmput301w22t20", profile.getUserUID());
        assertFalse(profile.getPermanent());
    }
}
