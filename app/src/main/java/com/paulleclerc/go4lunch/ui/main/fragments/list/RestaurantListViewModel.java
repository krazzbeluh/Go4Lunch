/*
 * RestaurantListViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 10:32 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.main.fragments.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.repository.PlacesRepository;

import java.util.List;

import javax.annotation.Nonnull;

public class RestaurantListViewModel extends AndroidViewModel {
    private final PlacesRepository placesRepository;

    private final MutableLiveData<List<Restaurant>> places = new MutableLiveData<>();

    public RestaurantListViewModel(@NonNull Application application) {
        super(application);
        this.placesRepository = new PlacesRepository();
    }

    public RestaurantListViewModel(@Nonnull Application application,
                                   PlacesRepository placesRepository) {
        super(application);
        this.placesRepository = placesRepository;
    }

    public void fetchPlaces(LatLng location) {
        placesRepository.fetchPlaces(location, this.places::setValue);
    }

    public LiveData<Restaurant> getPlaceDetail(String placeID) {
        MutableLiveData<Restaurant> restaurant = new MutableLiveData<>();

        placesRepository.fetchDetail(placeID, restaurant::setValue);

        return restaurant;
    }

    public LiveData<List<Restaurant>> getPlaces() {
        return places;
    }
}
