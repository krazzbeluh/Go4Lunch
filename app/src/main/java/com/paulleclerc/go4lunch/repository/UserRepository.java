/*
 * UserRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/8/20 10:42 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.net.Uri;

public class UserRepository {
    private final AuthRepository auth = new AuthRepository();
    private final FirestoreRepository firestore = new FirestoreRepository();
    private final FirStorageRepository storage = new FirStorageRepository();

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

    public void setUsername(String username) {
        firestore.setUsername(username);
    }

    public interface GetUserAvatarCompletion {
        void onComplete(String avatarUrl);
    }
}
