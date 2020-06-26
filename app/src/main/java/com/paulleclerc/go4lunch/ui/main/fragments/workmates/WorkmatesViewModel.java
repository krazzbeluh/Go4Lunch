/*
 * WorkmatesViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 10:55 AM.
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

import javax.annotation.Nonnull;

public class WorkmatesViewModel extends AndroidViewModel {
    private final WorkmatesRepository workmatesRepository;
    private final PlacesRepository placesRepository;

    private final MutableLiveData<List<Workmate>> workmates = new MutableLiveData<>();

    public WorkmatesViewModel(@NonNull Application application) {
        super(application);
        this.workmatesRepository = new WorkmatesRepository();
        this.placesRepository = new PlacesRepository();
    }

    public WorkmatesViewModel(@Nonnull Application application,
                              WorkmatesRepository workmatesRepository,
                              PlacesRepository placesRepository) {
        super(application);
        this.workmatesRepository = workmatesRepository;
        this.placesRepository = placesRepository;
    }

    public LiveData<List<Workmate>> getWorkmates() {
        return workmates;
    }

    public void fetchWorkmates() {
        workmatesRepository.fetchWorkmates(workmates -> {
            if (workmates != null) this.workmates.setValue(workmates);
        });
    }

    public LiveData<Restaurant> fetchRestaurant(String id) {
        MutableLiveData<Restaurant> restaurantLiveData = new MutableLiveData<>();
        placesRepository.fetchDetail(id, restaurantLiveData::setValue);
        return restaurantLiveData;
    }
}
