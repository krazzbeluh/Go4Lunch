/*
 * FirestoreRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/19/20 3:36 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreRepository {
    private static final String TAG = FirestoreRepository.class.getSimpleName();
    private static final String KEY_USER_COLLECTION = "User";
    public static final String KEY_AVATAR_NAME = "avatarName";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_CHOSEN_PLACE_ID = "chosenPlaceId";
    private static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_ALLOW_NOTIFICATIONS = "allowNotifications";

    private final FirStorageRepository storage;
    private final AuthRepository auth;


    private final FirebaseFirestore db;

    public FirestoreRepository() {
        db = FirebaseFirestore.getInstance();
        auth = new AuthRepository();
        storage = new FirStorageRepository();
    }

    public FirestoreRepository(FirebaseFirestore db, AuthRepository auth, FirStorageRepository storage) {
        this.db = db;
        this.auth = auth;
        this.storage = storage;
    }

    void getUserAvatarUrl(String userId, GetAvatarUrlCompletion completion) {
        db.collection(KEY_USER_COLLECTION).document(userId).addSnapshotListener((documentSnapshot, e) -> storage.getUserAvatar(getAvatarUri(documentSnapshot, e), (success, uri) -> {
            if (uri != null) completion.onAvatarChange(uri.toString());
        }));
    }

    public String getAvatarUri(DocumentSnapshot documentSnapshot, Throwable e) {
        if (e != null || documentSnapshot == null) {
            Log.e(TAG, "getUserAvatarUrl: ", e);
            return null;
        } else {
            return documentSnapshot.getString(KEY_AVATAR_NAME);
        }
    }

    public void setNewUserAvatar(String avatarFileName) {
        db.collection(KEY_USER_COLLECTION)
                .document(auth.getUid())
                .update(KEY_AVATAR_NAME, avatarFileName)
                .addOnFailureListener(e -> Log.e(TAG, "setNewUserAvatar: ", e));
    }

    public void getUsername(GetUsernameCompletion completion) {
        db.collection(KEY_USER_COLLECTION)
                .document(auth.getUid()).get()
                .addOnCompleteListener(task -> completion.onComplete(getUsernameFromResult(task)));
    }

    public String getUsernameFromResult(Task<DocumentSnapshot> task) {
        DocumentSnapshot documentSnapshot = task.getResult();
        if (task.isSuccessful() && documentSnapshot != null) {
            return documentSnapshot.getString(KEY_USERNAME);
        } else {
            Log.e(TAG, "getUsernameFromResult: ", task.getException());
            return null;
        }
    }

    public void setUsername(String username) {
        db.collection(KEY_USER_COLLECTION)
                .document(auth.getUid())
                .update(KEY_USERNAME, username)
                .addOnFailureListener(e -> Log.e(TAG, "setUsername: ", e));
    }

    public void setAllowNotifications(boolean allowNotifications) {
        db.collection(KEY_USER_COLLECTION).document(auth.getUid())
                .update(KEY_ALLOW_NOTIFICATIONS, allowNotifications)
                .addOnFailureListener(e -> Log.e(TAG, "setAllowNotifications: ", e));
    }

    public void getChosenPlaceId(GetPlaceIdCompletion completion) {
        db.collection(KEY_USER_COLLECTION).document(auth.getUid())
                .addSnapshotListener((documentSnapshot, e) -> completion.onComplete(getPlaceId(documentSnapshot, e)));
    }

    public String getPlaceId(DocumentSnapshot documentSnapshot, Throwable e) {
        if (e == null && documentSnapshot != null) {
            return documentSnapshot.getString(KEY_CHOSEN_PLACE_ID);
        } else {
            Log.e(TAG, "getChosenPlaceId: ", e);
            return null;
        }
    }

    public void getAllowNotifications(GetAllowNotificationsCompletion completion) {
        db.collection(KEY_USER_COLLECTION).document(auth.getUid()).get()
                .addOnCompleteListener(documentSnapshot -> completion.onComplete(getAreNotificationsAllowed(documentSnapshot)));
    }

    public Boolean getAreNotificationsAllowed(Task<DocumentSnapshot> task) {
        DocumentSnapshot documentSnapshot = task.getResult();
        if (task.isSuccessful() && documentSnapshot != null) {
            Boolean allowNotifications = documentSnapshot.getBoolean(KEY_ALLOW_NOTIFICATIONS);
            return allowNotifications == null ? true : allowNotifications;
        } else {
            Log.e(TAG, "getAllowNotifications: ", task.getException());
            return null;
        }
    }

    public void setChosenPlaceId(String placeId, SetChosenPlaceIdCompletion completion) {
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
