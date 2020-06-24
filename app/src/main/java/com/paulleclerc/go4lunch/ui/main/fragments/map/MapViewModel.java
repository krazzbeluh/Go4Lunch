/*
 * MapViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 9:42 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.main.fragments.map;

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

public class MapViewModel extends AndroidViewModel {
    private final PlacesRepository placesRepository;

    private final MutableLiveData<List<Restaurant>> places = new MutableLiveData<>();

    public MapViewModel(@NonNull Application application) {
        super(application);
        this.placesRepository = new PlacesRepository();
    }

    public MapViewModel(@NonNull Application application,
                        @Nonnull PlacesRepository placesRepository) {
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

    public void setPlaces(List<Restaurant> restaurants) {
        places.setValue(restaurants);
    }
}
