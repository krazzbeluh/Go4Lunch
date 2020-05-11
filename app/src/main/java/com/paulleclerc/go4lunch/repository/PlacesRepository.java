package com.paulleclerc.go4lunch.repository;

import android.content.res.Resources;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.closures.FetchPlacesCompletion;

public class PlacesRepository {
    private static final String TAG = PlacesRepository.class.getSimpleName();

    public void fetchPlaces(LatLng position, FetchPlacesCompletion completion) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(Resources.getSystem().getString(R.string.google_maps_and_places_key))
                .build();

        PlacesSearchResponse request = new PlacesSearchResponse();

        try {
            request = PlacesApi.nearbySearchQuery(context, convertLatLng(position))
                    .rankby(RankBy.DISTANCE)
                    .type(PlaceType.RESTAURANT)
                    .await();
        } catch (Exception e) {
            Log.e(TAG, "requiresAccessLocationPermission: ", e);
        }

        completion.onComplete(request.results);
    }

    private com.google.maps.model.LatLng convertLatLng(@NonNull LatLng latLng) {
        return new com.google.maps.model.LatLng(latLng.latitude, latLng.longitude);
    }
}