package com.example.qrscore;

import static org.junit.Assert.*;

import com.example.qrscore.model.Account;
import com.example.qrscore.model.QRCode;

import org.junit.Test;

import java.util.ArrayList;

public class AccountTest {
    @Test
    public void testBlankAccount() {
        Account acc = new Account("test_id");
        assertEquals("test_id", acc.getUserUID());
        assertNotNull(acc.getProfile());
        assertNotNull(acc.getQRCodesList());
        assertEquals(0, Integer.parseInt(acc.getTotalScore()));
        assertEquals(0, Integer.parseInt(acc.getHiscore()));
        assertEquals(0, Integer.parseInt(acc.getTotalScanned()));
        assertEquals(0, Integer.parseInt(acc.getRankTotalScore()));
        assertEquals(0, Integer.parseInt(acc.getRankHiscore()));
        assertEquals(0, Integer.parseInt(acc.getRankTotalScanned()));
    }

    @Test
    public void testAccountWithValues() {
        Account acc = new Account("test_id", "22", "2", "20",
                "3", "1", "8");
        assertEquals("test_id", acc.getUserUID());
        assertNotNull(acc.getProfile());
        assertNotNull(acc.getQRCodesList());
        assertEquals(22, Integer.parseInt(acc.getTotalScore()));
        assertEquals(20, Integer.parseInt(acc.getHiscore()));
        assertEquals(2, Integer.parseInt(acc.getTotalScanned()));
        assertEquals(3, Integer.parseInt(acc.getRankTotalScore()));
        assertEquals(8, Integer.parseInt(acc.getRankHiscore()));
        assertEquals(1, Integer.parseInt(acc.getRankTotalScanned()));
    }

    @Test
    public void testGetByHash() {
        Account acc = new Account("test_id");
        ArrayList<QRCode> qrs = new ArrayList<QRCode>();
        for (Integer i = 0; i < 5; i++) {
            qrs.add(new QRCode("hash"+i.toString()));
        }
        acc.setQRCodesList(qrs);

        assertEquals(qrs.get(2), acc.getQRByHash("hash2"));
        assertNull(acc.getQRByHash("hfdkjsfh"));
    }

    @Test
    public void testQRRemove() {
        Account acc = new Account("test_id");
        ArrayList<QRCode> qrs = new ArrayList<QRCode>();
        qrs.add(new QRCode("hash1"));
        qrs.add(new QRCode("hash2"));
        acc.setQRCodesList(qrs);
        acc.removeQR("hash1");

        assertEquals(1, acc.getQRCodesList().size());
    }
}
