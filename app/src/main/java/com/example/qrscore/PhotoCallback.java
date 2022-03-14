package com.example.qrscore;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

public interface PhotoCallback {
    void onCallback(Uri downloadURL);
}
