package com.example.qrscore;

import static org.junit.Assert.*;

import com.example.qrscore.model.QRCode;

import org.junit.Test;

public class QRCodeTest {
    @Test
    public void testHashConstructor() {
        QRCode qr = new QRCode("hash");
        assertEquals("hash", qr.getHash());
        assertNotNull(qr.getHasScanned());
        assertEquals(0, qr.getHasScanned().size());
    }

    @Test
    public void testDisplayConstructor() {
        QRCode qr = new QRCode("hash", "11");
        assertEquals("hash", qr.getHash());
        assertEquals("11", qr.getQRScore());
        assertNull(qr.getId());
    }

    @Test
    public void testAddComment() {
        QRCode qr = new QRCode("hash");
        qr.addToHasScanned("user1");

        assertEquals(1, qr.getHasScanned().size());
    }

    @Test
    public void testScoreCalc() {
        QRCode qr1 = new QRCode("00hjdf432kshf0hjdfkshfjds000whqe2jknr302");
        QRCode qr2 = new QRCode("0000ds0000jk,.';/'fdlsjg0000j37284956342089;'.;'.]00000");
        assertEquals("422", qr1.getQRScore());
        assertEquals("422", qr1.getId());
        assertEquals("184001", qr2.getQRScore());
        assertEquals("184001", qr2.getId());
    }
}
