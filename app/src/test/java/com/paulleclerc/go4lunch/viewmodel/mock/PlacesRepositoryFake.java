/*
 * PlacesRepositoryFake.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 4:06 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel.mock;

import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.repository.PlacesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class PlacesRepositoryFake extends PlacesRepository {
    List<Restaurant> restaurantsQueue = new ArrayList<>();
    List<List<Restaurant>> restaurantsListsQueue = new ArrayList<>();
    List<Boolean> booleansQueue = new ArrayList<>();

    public PlacesRepositoryFake() {
        super(null, null, null, null);
    }

    public void addRestaurantToQueue(Restaurant restaurant) {
        restaurantsQueue.add(restaurant);
    }

    public void addRestaurantsListToQueue(List<Restaurant> restaurants) {
        restaurantsListsQueue.add(restaurants);
    }

    String likedPlace;

    @Override
    public void fetchDetail(@Nonnull String placeId, FetchDetailsCompletion completion) {
        completion.onComplete(restaurantsQueue.remove(0));
    }

    @Override
    public void fetchPlaces(LatLng userPosition, FetchPlacesCompletion completion) {
        completion.onComplete(restaurantsListsQueue.remove(0));
    }

    String dislikedPlace;

    public void addBoolToQueue(Boolean bool) {
        booleansQueue.add(bool);
    }

    @Override
    public void getIsLiked(String id, LikeRestaurantCompletion completion) {
        completion.onComplete(booleansQueue.remove(0));
    }

    @Override
    public void likePlace(String id, LikeRestaurantCompletion completion) {
        likedPlace = id;
        completion.onComplete(true);
    }

    public String getLikedPlace() {
        return likedPlace;
    }

    @Override
    public void dislikePlace(String id, LikeRestaurantCompletion completion) {
        dislikedPlace = id;
        completion.onComplete(false);
    }

    public String getDislikedPlace() {
        return dislikedPlace;
    }
}
