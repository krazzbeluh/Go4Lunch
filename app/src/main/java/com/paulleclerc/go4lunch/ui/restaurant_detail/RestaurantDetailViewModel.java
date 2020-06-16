/*
 * RestaurantDetailViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/16/20 11:58 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.restaurant_detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.repository.PlacesRepository;
import com.paulleclerc.go4lunch.repository.UserRepository;

public class RestaurantDetailViewModel extends AndroidViewModel {
    public RestaurantDetailViewModel(@NonNull Application application) {
        super(application);
    }

    private final PlacesRepository placesRepository = new PlacesRepository();
    private final UserRepository userRepository = new UserRepository();

    private final MutableLiveData<Restaurant.RestaurantDetails> placeDetail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLiked = new MutableLiveData<>();
    private final MutableLiveData<String> chosenRestaurantId = new MutableLiveData<>();
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();

    LiveData<String> getAlertMessage() {
        return alertMessage;
    }

    LiveData<Restaurant.RestaurantDetails> getPlaceDetail(Restaurant restaurant) {
        placesRepository.fetchDetail(restaurant.id, restaurantWithDetail -> this.placeDetail.setValue(restaurantWithDetail.getDetails()));

        return placeDetail;
    }

    LiveData<Boolean> getIsLiked(Restaurant restaurant) {
        if (isLiked.getValue() == null)
            placesRepository.getIsLiked(restaurant.id, (success, isLiked) -> {
                if (success && isLiked != null) this.isLiked.setValue(isLiked);
            });
        return isLiked;
    }

    LiveData<String> getChosenRestaurantId() {
        userRepository.getChosenPlaceId(chosenRestaurantId::setValue);
        return chosenRestaurantId;
    }

    void chooseRestaurant(Restaurant restaurant) {
        userRepository.setChosenRestaurant(restaurant.id, () -> this.alertMessage.setValue(getApplication().getApplicationContext().getString(R.string.impossible_operation)));
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
