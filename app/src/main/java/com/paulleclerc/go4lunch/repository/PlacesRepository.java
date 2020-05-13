package com.paulleclerc.go4lunch.repository;

import android.content.res.Resources;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.paulleclerc.go4lunch.BuildConfig;
import com.paulleclerc.go4lunch.closures.FetchPlacesCompletion;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class PlacesRepository {
    private static final Map<LatLng, PlacesSearchResult[]> placesCache = new HashMap<>();

    private static final String TAG = PlacesRepository.class.getSimpleName();

    public void fetchPlaces(LatLng position, FetchPlacesCompletion completion) {
        PlacesSearchResult[] places = placesCache.get(position);
        if (places != null) {
            completion.onComplete(places);
            return;
        }

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(BuildConfig.GOOGLE_MAPS_AND_PLACES_KEY)
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

        PlacesSearchResult[] results = request.results;

        placesCache.put(position, results);

        completion.onComplete(results);
    }

    private com.google.maps.model.LatLng convertLatLng(@NonNull LatLng latLng) {
        return new com.google.maps.model.LatLng(latLng.latitude, latLng.longitude);
    }
}