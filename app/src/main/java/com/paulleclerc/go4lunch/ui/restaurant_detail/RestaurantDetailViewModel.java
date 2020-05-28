/*
 * RestaurantDetailViewModel.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/27/20 5:13 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.restaurant_detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.repository.PlacesRepository;

public class RestaurantDetailViewModel extends AndroidViewModel {
    public RestaurantDetailViewModel(@NonNull Application application) {
        super(application);
    }

    private final PlacesRepository placesRepository = new PlacesRepository(getApplication().getApplicationContext());

    private final MutableLiveData<Restaurant.RestaurantDetails> placeDetail = new MutableLiveData<>();

    LiveData<Restaurant.RestaurantDetails> getPlaceDetail(Restaurant restaurant) {
        placesRepository.fetchDetail(restaurant, placeDetail::setValue);

        return placeDetail;
    }
}
