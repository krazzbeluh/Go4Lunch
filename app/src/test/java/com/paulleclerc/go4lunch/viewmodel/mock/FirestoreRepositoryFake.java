/*
 * FirestoreRepositoryFake.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 10:16 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel.mock;

import com.paulleclerc.go4lunch.repository.FirestoreRepository;

import java.util.ArrayList;
import java.util.List;

public class FirestoreRepositoryFake extends FirestoreRepository {
    List<String> stringQueue = new ArrayList<>();

    public FirestoreRepositoryFake() {
        super(null, null, null);
    }

    public void addStringToQueue(String string) {
        stringQueue.add(string);
    }

    @Override
    public void getUsername(GetUsernameCompletion completion) {
        completion.onComplete(stringQueue.remove(0));
    }
}
