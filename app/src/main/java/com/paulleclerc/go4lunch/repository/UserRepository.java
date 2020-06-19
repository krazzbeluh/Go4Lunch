/*
 * UserRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/19/20 3:36 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.paulleclerc.go4lunch.model.Restaurant;

public class UserRepository {
    private static final String TAG = UserRepository.class.getSimpleName();

    private final AuthRepository auth;
    private final FirestoreRepository firestore;
    private final FirStorageRepository storage;
    private final PlacesRepository placesRepository;

    public UserRepository() {
        auth = new AuthRepository();
        firestore = new FirestoreRepository();
        storage = new FirStorageRepository();
        placesRepository = new PlacesRepository();
    }

    public UserRepository(AuthRepository auth, FirestoreRepository firestore, FirStorageRepository storage, PlacesRepository placesRepository) {
        this.auth = auth;
        this.firestore = firestore;
        this.storage = storage;
        this.placesRepository = placesRepository;
    }

    public void getUserAvatar(GetUserAvatarCompletion completion) {
        String uid = auth.getUid();
        if (uid != null) firestore.getUserAvatarUrl(uid, completion::onComplete);
    }

    public void getChosenRestaurant(GetChosenRestaurantCompletion completion) {
        firestore.getChosenPlaceId(placeId -> {
            if (placeId == null) completion.onComplete(null);
            else placesRepository.fetchDetail(placeId, completion::onComplete);
        });
    }

    public void setFCMToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> firestore.setFCMToken(getToken(task)));
    }

    public String getToken(Task<InstanceIdResult> task) {
        InstanceIdResult result = task.getResult();

        if (!task.isSuccessful() || result == null) {
            Log.e(TAG, "setFCMToken: ", task.getException());
            return null;
        }

        return result.getToken();
    }

    public interface GetUserAvatarCompletion {
        void onComplete(String avatarUrl);
    }

    public interface GetChosenRestaurantCompletion {
        void onComplete(Restaurant restaurant);
    }
}
