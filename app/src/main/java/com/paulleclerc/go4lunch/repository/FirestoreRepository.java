/*
 * FirestoreRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/17/20 4:36 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicReference;

public class FirestoreRepository {
    private static final String TAG = FirestoreRepository.class.getSimpleName();
    private static final String KEY_USER_COLLECTION = "User";
    private static final String KEY_AVATAR_NAME = "avatarName";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_CHOSEN_PLACE_ID = "chosenPlaceId";
    private static final String KEY_FCM_TOKEN = "fcmToken";
    private static final String KEY_ALLOW_NOTIFICATIONS = "allowNotifications";

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

    void getUsername(GetUsernameCompletion completion) {
        db.collection(KEY_USER_COLLECTION)
                .document(auth.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> completion.onComplete(documentSnapshot.getString(KEY_USERNAME)))
                .addOnFailureListener(e -> Log.e(TAG, "onFailure: ", e));
    }

    void setUsername(String username) {
        db.collection(KEY_USER_COLLECTION)
                .document(auth.getUid())
                .update(KEY_USERNAME, username)
                .addOnFailureListener(e -> Log.e(TAG, "setUsername: ", e));
    }

    void setAllowNotifications(boolean allowNotifications) {
        db.collection(KEY_USER_COLLECTION)
                .document(auth.getUid())
                .update(KEY_ALLOW_NOTIFICATIONS, allowNotifications)
                .addOnFailureListener(e -> Log.e(TAG, "setAllowNotifications: ", e));
    }

    void getChosenPlaceId(GetPlaceIdCompletion completion) {
        db.collection(KEY_USER_COLLECTION)
                .document(auth.getUid())
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e == null && documentSnapshot != null) {
                        completion.onComplete(documentSnapshot.getString(KEY_CHOSEN_PLACE_ID));
                    } else {
                        completion.onComplete(null);
                        Log.e(TAG, "getChosenPlaceId: ", e);
                    }
                });
    }

    void getAllowNotifications(GetAllowNotificationsCompletion completion) {
        db.collection(KEY_USER_COLLECTION)
                .document(auth.getUid())
                .get()
                .addOnCompleteListener(documentSnapshot -> {
                    DocumentSnapshot result = documentSnapshot.getResult();
                    if (documentSnapshot.isSuccessful() && result != null) {
                        completion.onComplete(result.getBoolean(KEY_ALLOW_NOTIFICATIONS));
                    } else {
                        completion.onComplete(null);
                        Log.e(TAG, "getAllowNotifications: ", documentSnapshot.getException());
                    }
                });
    }

    void setChosenPlaceId(String placeId, SetChosenPlaceIdCompletion completion) {
        db.collection(KEY_USER_COLLECTION)
                .document(auth.getUid())
                .update(KEY_CHOSEN_PLACE_ID, placeId)
                .addOnFailureListener(e -> {
                    Log.e(TAG, "setChosenPlaceId: ", e);
                    completion.onFailure();
                });
    }

    void setFCMToken(String token) {
        db.collection(KEY_USER_COLLECTION)
                .document(auth.getUid())
                .update(KEY_FCM_TOKEN, token);
    }

    interface GetAvatarUrlCompletion {
        void onAvatarChange(String avatarUrl);
    }

    public interface GetUsernameCompletion {
        void onComplete(String username);
    }

    public interface GetPlaceIdCompletion {
        void onComplete(String placeId);
    }

    public interface GetAllowNotificationsCompletion {
        void onComplete(Boolean allowNotifications);
    }

    public interface SetChosenPlaceIdCompletion {
        void onFailure();
    }
}
