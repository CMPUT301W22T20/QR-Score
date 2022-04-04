package com.example.qrscore;

import com.example.qrscore.controller.AccountController;
import com.example.qrscore.controller.QRCodeController;
import com.example.qrscore.model.Account;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

private class MockAccount extends Account {

}

public class QRCodeControllerTest {
    private QRCodeController qrCodeController = new QRCodeController();
    private Account account = new Account("TestAccount");
    private AccountController accountController = new AccountController();

    @Test
    public void testAddQRCode() {

    }

    @Test
    public void testRemoveQRCode() {

    }

    @Test
    public void testRemoveHighestScoringQRCodeWithOtherQRCodesScanned() {

    }

    @Test
    public void testAddAndRemoveTheSameQRCodeMultipleTimes() {

    }

    @Test
    public void testAddAndRemoveTheSameQRCodeMultipleTimes() {

    }
}
