/*
 * PlaceService.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/9/20 2:11 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.network;

import com.paulleclerc.go4lunch.BuildConfig;
import com.paulleclerc.go4lunch.network.restaurant_detail_response.RestaurantDetailResponse;
import com.paulleclerc.go4lunch.network.restaurant_response.RestaurantSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceService {
    @GET("maps/api/place/nearbysearch/json?key=" + BuildConfig.GOOGLE_MAPS_AND_PLACES_KEY + "&rankby=distance&type=restaurant")
    Call<RestaurantSearchResponse> getNearbyRestaurants(@Query("location") String location);

    @GET("maps/api/place/details/json?key=" + BuildConfig.GOOGLE_MAPS_AND_PLACES_KEY + "&fields=formatted_phone_number,website")
    Call<RestaurantDetailResponse> getPlaceDetail(@Query("place_id") String placeID);
}
