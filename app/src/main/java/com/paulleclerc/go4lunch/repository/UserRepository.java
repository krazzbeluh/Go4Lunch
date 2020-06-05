/*
 * UserRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/5/20 11:24 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.net.Uri;

public class UserRepository {
    private final AuthRepository auth = new AuthRepository();
    private final FirestoreRepository firestore = new FirestoreRepository();
    private final FirStorageRepository storage = new FirStorageRepository();

    public void getUserAvatar(GetUserInterfaceCompletion completion) {
        String uid = auth.getUid();
        assert uid != null;
        firestore.getUserAvatarUrl(uid, completion::onComplete);
    }

    public void setUserAvatar(Uri avatarUri) {
        storage.saveUserAvatar(avatarUri, firestore::setNewUserAvatar);
    }

    public interface GetUserInterfaceCompletion {
        void onComplete(String avatarUrl);
    }
}
