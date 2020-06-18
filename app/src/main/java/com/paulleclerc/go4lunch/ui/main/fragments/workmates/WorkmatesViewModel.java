/*
 * WorkmatesViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/18/20 12:47 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.main.fragments.workmates;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.model.Workmate;
import com.paulleclerc.go4lunch.repository.PlacesRepository;
import com.paulleclerc.go4lunch.repository.WorkmatesRepository;

import java.util.List;

public class WorkmatesViewModel extends AndroidViewModel {
    private final WorkmatesRepository workmatesRepository = new WorkmatesRepository();
    private final PlacesRepository placesRepository = new PlacesRepository();

    private final MutableLiveData<List<Workmate>> workmates = new MutableLiveData<>();

    public WorkmatesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Workmate>> getWorkmates() {
        return workmates;
    }

    void fetchWorkmates() {
        workmatesRepository.fetchWorkmates((success, workmates) -> {
            if (success) this.workmates.setValue(workmates);
        });
    }

    LiveData<Restaurant> fetchRestaurant(String id) {
        MutableLiveData<Restaurant> restaurantLiveData = new MutableLiveData<>();
        placesRepository.fetchDetail(id, restaurantLiveData::setValue);
        return restaurantLiveData;
    }
}
