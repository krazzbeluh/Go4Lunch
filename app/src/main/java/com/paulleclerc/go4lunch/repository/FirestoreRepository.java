/*
 * FirestoreRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/5/20 11:24 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicReference;

public class FirestoreRepository {
    private static final String TAG = FirestoreRepository.class.getSimpleName();
    private static final String KEY_USER_COLLECTION = "User";
    private static final String KEY_AVATAR_NAME = "avatarName";

    private final FirStorageRepository storage = new FirStorageRepository();
    private final AuthRepository auth = new AuthRepository();


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    void getUserAvatarUrl(String userId, GetAvatarUrlCompletion completion) {
        AtomicReference<String> oldName = new AtomicReference<>();
        db.collection(KEY_USER_COLLECTION).document(userId).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null || documentSnapshot == null) {
                Log.e(TAG, "getUserAvatarUrl: ", e);
            } else {
                String name = documentSnapshot.getString(KEY_AVATAR_NAME);
                if (name == null || !name.equals(oldName.get())) {
                    oldName.set(name);

                    storage.getUserAvatar(name, (success, uri) -> {
                        if (uri != null) completion.onAvatarChange(uri.toString());
                    });
                }
            }
        });
    }

    void setNewUserAvatar(String avatarFileName) {
        db.collection(KEY_USER_COLLECTION)
                .document(auth.getUid())
                .update(KEY_AVATAR_NAME, avatarFileName)
                .addOnFailureListener(e -> Log.e(TAG, "setNewUserAvatar: ", e));
    }

    interface GetAvatarUrlCompletion {
        void onAvatarChange(String avatarUrl);
    }
}
