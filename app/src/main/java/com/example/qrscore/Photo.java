package com.example.qrscore;

import android.net.Uri;

public class Photo {
    private Uri imageUri;

    public Photo(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}
