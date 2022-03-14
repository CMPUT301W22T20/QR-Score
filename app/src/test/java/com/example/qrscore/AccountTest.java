package com.example.qrscore;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Random;

public class AccountTest {
    @Test
    public void testCreateBlank() {
        Account test = new Account("test_id");
        assertNotNull(test);
        assertEquals("test_id", test.getUserID());
        assertEquals(java.util.Optional.of(0), test.getTotalScore());
        assertEquals(java.util.Optional.of(0), test.getScanned());
    }

    @Test
    public void testCreateInts() {
        Account test = new Account("test_id", 2, 3);
        assertNotNull(test);
        assertEquals("test_id", test.getUserID());
        assertEquals(java.util.Optional.of(2), test.getTotalScore());
        assertEquals(java.util.Optional.of(3), test.getScanned());
    }
}
