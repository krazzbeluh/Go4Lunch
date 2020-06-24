/*
 * WorkmatesRepositoryFake.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 10:55 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel.mock;

import com.paulleclerc.go4lunch.model.Workmate;
import com.paulleclerc.go4lunch.repository.WorkmatesRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesRepositoryFake extends WorkmatesRepository {
    List<List<Workmate>> workmatesListQueue = new ArrayList<>();

    public WorkmatesRepositoryFake() {
        super(null, null, null);
    }

    public void addWorkmatesToQueue(List<Workmate> workmates) {
        workmatesListQueue.add(workmates);
    }

    @Override
    public void fetchWorkmates(FetchWorkmatesCompletion completion) {
        completion.onComplete(workmatesListQueue.remove(0));
    }
}
