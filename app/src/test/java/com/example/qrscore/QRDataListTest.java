package com.example.qrscore;

import static org.junit.Assert.*;
import org.junit.Test;

public class QRDataListTest {
    @Test
    public void testCreateList() {
        QRDataList test = new QRDataList();
        assertNotNull(test);
        assertNotNull(test.getQRCodes());
    }
}
