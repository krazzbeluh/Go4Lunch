/*
 * FirStorageRepository.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/27/20 5:13 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.paulleclerc.go4lunch.closures.GetUserAvatarUriCompletion;

class FirStorageRepository {
    private static final String TAG = FirStorageRepository.class.getSimpleName();

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    void getUserAvatar(String fileName, GetUserAvatarUriCompletion completion) {
        if (fileName == null) completion.onComplete(true, null);
        else {
            storage.getReference().child("Avatar").child(fileName).getDownloadUrl().addOnCompleteListener((task) -> {
                if (task.isSuccessful()) {
                    completion.onComplete(true, task.getResult());
                } else {
                    Log.e(TAG, "getUserAvatar: ", task.getException());
                    completion.onComplete(false, null);
                }
            });
        }
    }
}
