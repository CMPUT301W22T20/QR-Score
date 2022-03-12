package com.example.qrscore;

import android.util.Log;

import java.util.List;

public class QRList {
    private static final String TAG = "STATS";
    private List<QRCode> qrCodes;
    private Integer totalQRCodesScanned;
    private Integer sumOfScoresScanned;

    /**
     * Constructor for Stats class
     *
     */
    public QRList() {
        this.totalQRCodesScanned = 0;
        this.sumOfScoresScanned = 0;
    }

    /**
     * Returns the array of QR codes.
     *
     * @return
     *      a List of QR codes.
     */
    public List<QRCode> getQRCodes() {
        return qrCodes;
    }

    /**
     * Adds a QR code to the list.
     *
     * @param qr
     *      the QR code to add.
     */
    public void addQRCode(QRCode qr) {
        if (!qrCodes.contains(qr)) {
            qrCodes.add(qr);
        } else {
            Log.d(TAG, "Duplicate QR code. Skipped adding.");
        }
    }

    /**
     * Removes a QR code from the list if it exists.
     *
     * @param qr
     *      the QR code to remove.
     */
    public void removeQRCode(QRCode qr) {
        if (qrCodes.contains(qr)) {
            qrCodes.remove(qr);
        } else {
            Log.d(TAG, "QR code not in list.");
        }
    }

    /**
     * Returns the total number of QR codes a user has scanned.
     *
     * @return
     *      the sum of a user's scanned QR codes.
     */
    public Integer getTotalQRCodesScanned() {
        return this.totalQRCodesScanned;
    }

    /**
     * Returns the total sum of QR scores a user has scanned.
     *
     * @return
     *      the sum of a user's scanned QR scores.
     */
    public Integer getSumOfScoresScanned() {
        return this.sumOfScoresScanned;
    }

    /**
     * Calculates the total number of QR codes a user has scanned and updates the totalQRCodesScanned attribute.
     *
     */
    public void calculateTotalQRCodesScanned() {
        this.totalQRCodesScanned = this.qrCodes.size();
    }

    /**
     * Calculates the total sum of QR scores a user has scanned and updates the sumOfScoresScanned attribute.
     *
     */
    public void calculateSumOfScoresScanned() {
        Integer sum = 0;
        for (int i=0; i<(this.qrCodes.size()); i++) {
            sum += this.qrCodes.get(i).getQRScore();
        }
        this.sumOfScoresScanned = sum;
    }
}

