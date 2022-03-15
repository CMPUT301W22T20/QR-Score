package com.example.qrscore;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

/**
 * Purpose: Interface for photo call back.
 *
 * Outstanding Issues:
 *
 */
public interface PhotoCallback {
    void onCallback(Uri downloadURL);
}
