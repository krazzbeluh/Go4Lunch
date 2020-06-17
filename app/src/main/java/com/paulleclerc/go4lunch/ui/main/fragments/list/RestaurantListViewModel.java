/*
 * RestaurantListViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/17/20 5:33 PM.
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

public class RestaurantListViewModel extends AndroidViewModel {
    private final PlacesRepository placesRepository = new PlacesRepository();

    private final MutableLiveData<List<Restaurant>> places = new MutableLiveData<>();

    public RestaurantListViewModel(@NonNull Application application) {
        super(application);
    }

    void fetchPlaces(LatLng location) {
        placesRepository.fetchPlaces(location, this.places::setValue);
    }

    LiveData<Restaurant> getPlaceDetail(String placeID) {
        MutableLiveData<Restaurant> restaurant = new MutableLiveData<>();

        placesRepository.fetchDetail(placeID, restaurant::setValue);

        return restaurant;
    }

    LiveData<List<Restaurant>> getPlaces() {
        return places;
    }
}
