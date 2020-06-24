/*
 * PlacesRepositoryFake.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 9:42 AM.
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

    public PlacesRepositoryFake() {
        super(null, null, null, null);
    }

    public void addRestaurantToQueue(Restaurant restaurant) {
        restaurantsQueue.add(restaurant);
    }

    public void addRestaurantsListToQueue(List<Restaurant> restaurants) {
        restaurantsListsQueue.add(restaurants);
    }

    @Override
    public void fetchDetail(@Nonnull String placeId, FetchDetailsCompletion completion) {
        completion.onComplete(restaurantsQueue.remove(0));
    }

    @Override
    public void fetchPlaces(LatLng userPosition, FetchPlacesCompletion completion) {
        completion.onComplete(restaurantsListsQueue.remove(0));
    }
}
