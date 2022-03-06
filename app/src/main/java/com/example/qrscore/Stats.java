package com.example.qrscore;

import java.util.List;

public class Stats {
    private List<QRCode> qrCodes;
    private Integer totalQRCodesScanned;
    private Integer sumOfScoresScanned;

    public Stats() {
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
     * @param toAdd
     *      the QR code to add.
     */
    public void addQRCode(QRCode toAdd) {
        qrCodes.add(toAdd);
    }

    /**
     * Removes a QR code from the list if it exists.
     *
     * @param toRemove
     *      the QR code to remove.
     */
    public void removeQRCode(QRCode toRemove) {
        // Check if the QR code is in the list or not, throw exception if it isn't.
        qrCodes.remove(toRemove);
    }

    public Integer getTotalQRCodesScanned() {
        return this.totalQRCodesScanned;
    }

    public Integer getSumOfScoresScanned() {
        return this.sumOfScoresScanned;
    }

    public void calculateTotalQRCodesScanned() {
        this.totalQRCodesScanned = qrCodes.size();
    }

    public void calculateSumOfScoresScanned() {
        Integer sum = 0;
        for (int i=0; i<qrCodes.size(); i++) {
            sum += qrCodes.get(i).getQRScore();
        }
        this.sumOfScoresScanned = sum;
    }
}

