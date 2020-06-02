/*
 * RestaurantDetailViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/2/20 5:34 PM.
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

    private final PlacesRepository placesRepository = new PlacesRepository();

    private final MutableLiveData<Restaurant.RestaurantDetails> placeDetail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLiked = new MutableLiveData<>();

    LiveData<Restaurant.RestaurantDetails> getPlaceDetail(Restaurant restaurant) {
        placesRepository.fetchDetail(restaurant, placeDetail::setValue);

        return placeDetail;
    }

    LiveData<Boolean> getIsLiked(Restaurant restaurant) {
        if (isLiked.getValue() == null)
            placesRepository.getIsLiked(restaurant.id, (success, isLiked) -> {
                if (success && isLiked != null) this.isLiked.setValue(isLiked);
            });
        return isLiked;
    }

    void switchLike(Restaurant restaurant) {
        Boolean isLiked = this.isLiked.getValue();
        if (isLiked == null) return;

        if (!isLiked) {
            placesRepository.likePlace(restaurant.id, (success, isLiked1) -> {
                if (success && isLiked1 != null) this.isLiked.setValue(isLiked1);
            });
        } else {
            placesRepository.dislikePlace(restaurant.id, (success, isLiked1) -> {
                if (success && isLiked1 != null) this.isLiked.setValue(isLiked1);
            });
        }
    }
}
