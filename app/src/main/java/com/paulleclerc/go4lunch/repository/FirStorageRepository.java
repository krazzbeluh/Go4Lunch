/*
 * FirStorageRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/18/20 4:15 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.paulleclerc.go4lunch.closures.GetUserAvatarUriCompletion;

import java.util.UUID;

public class FirStorageRepository {
    private static final String KEY_AVATAR_DIRECTORY = "Avatar";
    private static final String TAG = FirStorageRepository.class.getSimpleName();

    private final FirebaseStorage storage;

    FirStorageRepository() {
        storage = FirebaseStorage.getInstance();
    }

    public FirStorageRepository(FirebaseStorage storage) {
        this.storage = storage;
    }

    void getUserAvatar(String fileName, GetUserAvatarUriCompletion completion) {
        if (fileName == null) completion.onComplete(true, null);
        else {
            storage.getReference().child(KEY_AVATAR_DIRECTORY).child(fileName).getDownloadUrl().addOnCompleteListener((task) -> {
                Uri uri = getUriFromTask(task);
                completion.onComplete(uri != null, uri);
            });
        }
    }

    public Uri getUriFromTask(Task<Uri> task) {
        if (task.isSuccessful()) {
            return task.getResult();
        } else {
            Log.e(TAG, "getUserAvatar: ", task.getException());
            return null;
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
