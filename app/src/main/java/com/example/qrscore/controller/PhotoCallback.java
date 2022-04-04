package com.example.qrscore.controller;

import android.net.Uri;

/**
 * Purpose: Interface for photo call back.
 *
 * Outstanding Issues:
 */
public interface PhotoCallback {
    void onCallback(Uri downloadURL);
}
