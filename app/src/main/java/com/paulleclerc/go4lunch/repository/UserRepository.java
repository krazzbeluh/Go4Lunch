/*
 * UserRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/5/20 11:58 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

public class UserRepository {
    private final AuthRepository auth = new AuthRepository();
    private final FirestoreRepository firestore = new FirestoreRepository();

    void getUserAvatar(GetUserInterfaceCompletion completion) {
        String uid = auth.getUid();
        assert uid != null;
        firestore.getUserAvatarUrl(uid, completion::onComplete);
    }

    public interface GetUserInterfaceCompletion {
        void onComplete(String avatarUrl);
    }
}
