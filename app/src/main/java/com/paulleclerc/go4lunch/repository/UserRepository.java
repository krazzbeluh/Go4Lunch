/*
 * UserRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/15/20 6:10 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.net.Uri;

import com.paulleclerc.go4lunch.model.Restaurant;

public class UserRepository {
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

    public void getChosenRestaurant(GetChosenRestaurantCompletion completion) {
        firestore.getChosenPlaceId(placeId -> {
            if (placeId == null) completion.onComplete(null);
            else placesRepository.fetchDetail(placeId, completion::onComplete);
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
}
