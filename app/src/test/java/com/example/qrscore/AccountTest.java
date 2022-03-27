package com.example.qrscore;

import static org.junit.Assert.*;

import com.example.qrscore.model.Account;

import org.junit.Test;

public class AccountTest {
    @Test
    public void testCreateBlank() {
        Account test = new Account("test_id");
        assertNotNull(test);
        assertEquals("test_id", test.getUserUID());
        assertEquals("0", test.getScore().toString());
        assertEquals("0", test.getScanned().toString());
    }

    @Test
    public void testCreateInts() {
        Account test = new Account("test_id", 2, 1, 3);
        assertNotNull(test);
        assertEquals("test_id", test.getUserUID());
        assertEquals("2", test.getScore().toString());
        assertEquals("3", test.getScanned().toString());
    }
}
