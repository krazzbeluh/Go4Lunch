package com.paulleclerc.go4lunch.repository;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.paulleclerc.go4lunch.BuildConfig;
import com.paulleclerc.go4lunch.closures.FetchPlacesCompletion;
import com.paulleclerc.go4lunch.model.Restaurant;

import java.text.DecimalFormat;
import java.util.*;

public class PlacesRepository {
    private static final Map<LatLng, List<Restaurant>> placesCache = new HashMap<>();

    private static final String TAG = PlacesRepository.class.getSimpleName();

    private final PlacesClient client;

    public PlacesRepository(PlacesClient client) {
        this.client = client;
    }

    public void fetchPlaces(LatLng position, FetchPlacesCompletion completion) {
        List<Restaurant> places = placesCache.get(position);
        if (places != null) {
            completion.onComplete(null);
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

        fetchPlacesDetails(results, position, restaurants -> {
            placesCache.put(position, restaurants);
            completion.onComplete(restaurants);
        });
    }

    private void fetchPlacesDetails(PlacesSearchResult[] placesSearchResults, LatLng position, FetchDetailsCallback callback) {
        List<Restaurant> restaurants = new ArrayList<>();
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.ADDRESS_COMPONENTS, Place.Field.RATING, Place.Field.PHOTO_METADATAS);

        for (PlacesSearchResult placesSearchResult: placesSearchResults) {
            FetchPlaceRequest request = FetchPlaceRequest.newInstance(placesSearchResult.placeId, placeFields);
            client.fetchPlace(request).addOnSuccessListener(fetchPlaceResponse -> {
                Place place = fetchPlaceResponse.getPlace();

                Double rate = place.getRating();
                Restaurant.Rate resRate;
                if (rate != null) {
                    rate = rate / 5 * 3;
                    resRate = (rate <= 1) ? Restaurant.Rate.BAD: (rate <= 2) ? Restaurant.Rate.MEDIUM : Restaurant.Rate.GOOD;
                } else {
                    resRate = Restaurant.Rate.UNKNOWN;
                }



                Restaurant restaurant = new Restaurant(placesSearchResult.placeId, place.getName(), place.getAddress(), "place.getPhotoMetadatas().get(0).getAttributions()", resRate, place.getLatLng(), getDistance(place.getLatLng(), position));
                restaurants.add(restaurant);

                if (placesSearchResult.placeId.equals(placesSearchResults[placesSearchResults.length - 1].placeId)) callback.onComplete(restaurants);
            }).addOnFailureListener(exception -> Log.e(TAG, "fetchPlacesDetails: ", exception));
        }

    }

    private Integer getDistance(@Nullable LatLng StartP, LatLng EndP) {
        if (StartP == null) return null;
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return (int) (Radius * c * 1000);
    }

    private interface FetchDetailsCallback {
        void onComplete(List<Restaurant> restaurants);
    }

    private com.google.maps.model.LatLng convertLatLng(@NonNull LatLng latLng) {
        return new com.google.maps.model.LatLng(latLng.latitude, latLng.longitude);
    }
}