/*
 * FirStorageRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/5/20 11:24 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.paulleclerc.go4lunch.closures.GetUserAvatarUriCompletion;

import java.util.UUID;

class FirStorageRepository {
    private static final String KEY_AVATAR_DIRECTORY = "Avatar";
    private static final String TAG = FirStorageRepository.class.getSimpleName();

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    void getUserAvatar(String fileName, GetUserAvatarUriCompletion completion) {
        if (fileName == null) completion.onComplete(true, null);
        else {
            storage.getReference().child(KEY_AVATAR_DIRECTORY).child(fileName).getDownloadUrl().addOnCompleteListener((task) -> {
                if (task.isSuccessful()) {
                    completion.onComplete(true, task.getResult());
                } else {
                    Log.e(TAG, "getUserAvatar: ", task.getException());
                    completion.onComplete(false, null);
                }
            });
        }
    }

    void saveUserAvatar(Uri avatarUri, SaveUserAvatar completion) {
        String fileName = UUID.randomUUID().toString() + ".jpeg";
        StorageReference directory = storage.getReference().child(KEY_AVATAR_DIRECTORY);
        StorageReference fileRef = directory.child(fileName);

        fileRef.putFile(avatarUri).addOnSuccessListener(taskSnapshot -> completion.onComplete(fileName)).addOnFailureListener(e -> Log.e(TAG, "saveUserAvatar: ", e));
    }

    public interface SaveUserAvatar {
        void onComplete(String fileName);
    }
}
