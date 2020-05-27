/*
 * PlaceClient.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/27/20 5:13 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.network;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.model.restaurant_response.RestaurantSearchResponse;
import com.paulleclerc.go4lunch.model.restaurant_response.Result;
import com.paulleclerc.go4lunch.network.restaurant_detail_response.RestaurantDetailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class PlaceClient {
    private static final String TAG = PlaceClient.class.getSimpleName();
    private static final String BASE_URL = "https://maps.googleapis.com/";

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final PlaceService service = retrofit.create(PlaceService.class);

    public void fetchRestaurants(LatLng location, FetchRestaurantsCompletion completion) {
        Call<RestaurantSearchResponse> call = service.getNearbyRestaurants(location.latitude + "," + location.longitude);
        call.enqueue(new Callback<RestaurantSearchResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<RestaurantSearchResponse> call, Response<RestaurantSearchResponse> response) {
                assert response.body() != null;

                completion.onComplete(response.body().getResults());
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<RestaurantSearchResponse> call, Throwable t) {
                completion.onComplete(null);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void fetchDetails(Restaurant restaurant, FetchDetailsCompletion completion) {
        Call<RestaurantDetailResponse> call = service.getPlaceDetail(restaurant.id);
        call.enqueue(new Callback<RestaurantDetailResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<RestaurantDetailResponse> call, Response<RestaurantDetailResponse> response) {
                assert response.body() != null;

                completion.onComplete(response.body().getResult());
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<RestaurantDetailResponse> call, Throwable t) {
                completion.onComplete(null);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public interface FetchRestaurantsCompletion {
        void onComplete(List<Result> restaurants);
    }

    public interface FetchDetailsCompletion {
        void onComplete(com.paulleclerc.go4lunch.network.restaurant_detail_response.Result details);
    }
}