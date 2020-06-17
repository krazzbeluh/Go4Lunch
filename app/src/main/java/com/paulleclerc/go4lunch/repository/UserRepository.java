/*
 * UserRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/17/20 3:34 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.paulleclerc.go4lunch.model.Restaurant;

public class UserRepository {
    private static final String TAG = UserRepository.class.getSimpleName();

    private final AuthRepository auth = new AuthRepository();
    private final FirestoreRepository firestore = new FirestoreRepository();
    private final FirStorageRepository storage = new FirStorageRepository();
    private final PlacesRepository placesRepository = new PlacesRepository();

    public void getUserAvatar(GetUserAvatarCompletion completion) {
        String uid = auth.getUid();
        assert uid != null;
        firestore.getUserAvatarUrl(uid, completion::onComplete);
    }

    public void setUserAvatar(Uri avatarUri) {
        storage.saveUserAvatar(avatarUri, firestore::setNewUserAvatar);
    }

    public void getUsername(FirestoreRepository.GetUsernameCompletion completion) {
        firestore.getUsername(completion);
    }

    public void getChosenPlaceId(GetChosenPlaceIdCompletion completion) {
        firestore.getChosenPlaceId(completion::onComplete);
    }

    public void getChosenRestaurant(GetChosenRestaurantCompletion completion) {
        getChosenPlaceId(placeId -> {
            if (placeId == null) completion.onComplete(null);
            else placesRepository.fetchDetail(placeId, completion::onComplete);
        });
    }

    public void setChosenRestaurant(String restaurantId, SetChosenPlaceIdCompletion completion) {
        firestore.setChosenPlaceId(restaurantId, completion::onFailure);
    }

    public void setFCMToken() {
        FirebaseInstanceId
                .getInstance()
                .getInstanceId()
                .addOnCompleteListener(task -> {
                    InstanceIdResult result = task.getResult();

                    if (!task.isSuccessful() || result == null) {
                        Log.w(TAG, "setFCMToken: ", task.getException());
                        return;
                    }

                    String token = result.getToken();
                    firestore.setFCMToken(token);
                });
    }

    public void setUsername(String username) {
        firestore.setUsername(username);
    }

    public interface GetUserAvatarCompletion {
        void onComplete(String avatarUrl);
    }

    public interface GetChosenRestaurantCompletion {
        void onComplete(Restaurant restaurant);
    }

    public interface GetChosenPlaceIdCompletion {
        void onComplete(String placeId);
    }

    public interface SetChosenPlaceIdCompletion {
        void onFailure();
    }
}
