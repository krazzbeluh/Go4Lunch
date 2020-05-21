package com.paulleclerc.go4lunch.network;

import com.paulleclerc.go4lunch.BuildConfig;
import com.paulleclerc.go4lunch.model.restaurant_response.RestaurantSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlaceService {
    @GET("maps/api/place/nearbysearch/json?key=" + BuildConfig.GOOGLE_MAPS_AND_PLACES_KEY + "&rankby=distance&type=restaurant")
    Call<RestaurantSearchResponse> getNearbyRestaurants(@Query("location") String location);
}
