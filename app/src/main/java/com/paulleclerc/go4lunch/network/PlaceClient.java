package com.paulleclerc.go4lunch.network;

import android.util.Log;
import com.paulleclerc.go4lunch.model.restaurant_response.RestaurantSearchResponse;
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

    public void fetchRestaurants() {
        Call<RestaurantSearchResponse> call = service.getNearbyRestaurants("49.0176,-0.8337");
        call.enqueue(new Callback<RestaurantSearchResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<RestaurantSearchResponse> call, Response<RestaurantSearchResponse> response) {
                assert response.body() != null;
                Log.d(TAG, "onResponse: " + response.body().getResults().get(0).getName());
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<RestaurantSearchResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}