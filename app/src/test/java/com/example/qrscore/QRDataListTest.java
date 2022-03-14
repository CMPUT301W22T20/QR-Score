package com.example.qrscore;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class QRDataListTest {
    private QRDataList test;
    @Before
    public void createQRList() {
        test = new QRDataList();
    }

    @Test
    public void testCreateList() {
        assertNotNull(test);
        assertNotNull(test.getQRCodes());
    }

    @Test
    public void testAdd() {
        test.addQRCode(new QRCode("test_hash"));
        assertEquals(1, test.getQRCodes().size());
    }

    @Test
    public void testRemove() {
        QRCode qr = new QRCode("test_hash");
        test.addQRCode(qr);
        test.removeQRCode(qr);
        assertEquals(0, test.getQRCodes().size());
    }

    @Test
    public void testGetTotal() {
        test.addQRCode(new QRCode("test1"));
        test.addQRCode(new QRCode("test2"));
        test.addQRCode(new QRCode("test3"));
        assertEquals(java.util.Optional.of(3), test.getTotalQRCodesScanned());
    }

    @Test
    public void testScoreSum() {
        test.addQRCode(new QRCode("test1"));
        test.addQRCode(new QRCode("test2"));
        test.addQRCode(new QRCode("test3"));
        int sum = test.getQRCodes().get(0).getQRScore();
        sum += test.getQRCodes().get(1).getQRScore();
        sum += test.getQRCodes().get(2).getQRScore();
        assertEquals(java.util.Optional.of(sum), test.getSumOfScoresScanned());
    }
}
