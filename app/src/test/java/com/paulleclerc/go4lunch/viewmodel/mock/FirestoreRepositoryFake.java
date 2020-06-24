/*
 * FirestoreRepositoryFake.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 4:06 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel.mock;

import com.paulleclerc.go4lunch.repository.FirestoreRepository;

import java.util.ArrayList;
import java.util.List;

public class FirestoreRepositoryFake extends FirestoreRepository {
    List<String> stringQueue = new ArrayList<>();
    boolean shouldFail = false;

    public FirestoreRepositoryFake() {
        super(null, null, null);
    }

    public void addStringToQueue(String string) {
        stringQueue.add(string);
    }

    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }

    @Override
    public void getUsername(GetUsernameCompletion completion) {
        completion.onComplete(stringQueue.remove(0));
    }

    @Override
    public void getChosenPlaceId(GetPlaceIdCompletion completion) {
        completion.onComplete(stringQueue.remove(0));
    }

    @Override
    public void setChosenPlaceId(String placeId, SetChosenPlaceIdCompletion completion) {
        if (shouldFail) completion.onFailure();
    }
}
