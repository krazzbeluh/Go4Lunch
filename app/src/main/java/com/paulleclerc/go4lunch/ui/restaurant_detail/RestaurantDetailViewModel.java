/*
 * RestaurantDetailViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 4:06 PM.
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
import com.paulleclerc.go4lunch.repository.FirestoreRepository;
import com.paulleclerc.go4lunch.repository.PlacesRepository;

public class RestaurantDetailViewModel extends AndroidViewModel {
    private final PlacesRepository placesRepository;
    private final FirestoreRepository firestore;

    private final MutableLiveData<Restaurant.RestaurantDetails> placeDetail =
            new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLiked = new MutableLiveData<>();
    private final MutableLiveData<String> chosenRestaurantId = new MutableLiveData<>();
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();


    public RestaurantDetailViewModel(@NonNull Application application) {
        super(application);
        this.placesRepository = new PlacesRepository();
        this.firestore = new FirestoreRepository();
    }

    public RestaurantDetailViewModel(@NonNull Application application,
                                     PlacesRepository placesRepository,
                                     FirestoreRepository firestoreRepository) {
        super(application);
        this.placesRepository = placesRepository;
        this.firestore = firestoreRepository;
    }

    public LiveData<String> getAlertMessage() {
        return alertMessage;
    }

    public LiveData<Restaurant.RestaurantDetails> getPlaceDetail(Restaurant restaurant) {
        placesRepository.fetchDetail(restaurant.id,
                restaurantWithDetail -> this.placeDetail.setValue(restaurantWithDetail.getDetails()));

        return placeDetail;
    }

    public LiveData<Boolean> getIsLiked(Restaurant restaurant) {
        if (isLiked.getValue() == null)
            placesRepository.getIsLiked(restaurant.id, isLiked -> {
                if (isLiked != null) this.isLiked.setValue(isLiked);
            });
        return isLiked;
    }

    public LiveData<String> getChosenRestaurantId() {
        firestore.getChosenPlaceId(chosenRestaurantId::setValue);
        return chosenRestaurantId;
    }

    public void chooseRestaurant(Restaurant restaurant) {
        firestore.setChosenPlaceId(restaurant.id,
                () -> this.alertMessage.setValue(getApplication().getApplicationContext().getString(R.string.impossible_operation)));
    }

    public void switchLike(Restaurant restaurant) {
        Boolean isLiked = this.isLiked.getValue();
        if (isLiked == null) return;

        if (!isLiked) {
            placesRepository.likePlace(restaurant.id, isLiked1 -> {
                if (isLiked1 != null) this.isLiked.setValue(isLiked1);
            });
        } else {
            placesRepository.dislikePlace(restaurant.id, isLiked1 -> {
                if (isLiked1 != null) this.isLiked.setValue(isLiked1);
            });
        }
    }
}
