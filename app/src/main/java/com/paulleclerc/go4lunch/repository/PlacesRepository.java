package com.paulleclerc.go4lunch.repository;

import androidx.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.closures.FetchPlacesCompletion;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.model.restaurant_response.Result;
import com.paulleclerc.go4lunch.network.PlaceClient;

import java.util.*;

public class PlacesRepository {
    private static final Map<LatLng, List<Restaurant>> placesCache = new HashMap<>();

    private static final String TAG = PlacesRepository.class.getSimpleName();
    private final PlaceClient client;

    public PlacesRepository(PlaceClient client) {
        this.client = client;
    }

    public PlacesRepository() {
        this.client = new PlaceClient();
    }

    public void fetchPlaces(LatLng userPosition, FetchPlacesCompletion completion) {
        List<Restaurant> restaurants = placesCache.get(userPosition);
        if (restaurants != null) {
            completion.onComplete(restaurants);
        } else {
            client.fetchRestaurants(userPosition, results -> {
                List<Restaurant> restaurantList = new ArrayList<>();
                for (Result result : results) {

                    Double rating = result.getRating();
                    Restaurant.Rate rate;
                    if (rating == null) {
                        rate = Restaurant.Rate.UNKNOWN;
                    } else {
                        rating = result.getRating() / 5 * 3;

                        if (rating < 1) {
                            rate = Restaurant.Rate.BAD;
                        } else if (rating < 2) {
                            rate = Restaurant.Rate.MEDIUM;
                        } else {
                            rate = Restaurant.Rate.GOOD;
                        }
                    }

                    LatLng restaurantLocation = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());

                    String photoReference;
                    if (result.getPhotos() == null) {
                        photoReference = null;
                    } else {
                        photoReference = result.getPhotos().get(0).getPhotoReference();
                    }

                    Boolean isOpened;
                    if (result.getOpeningHours() == null) isOpened = null;
                    else isOpened = result.getOpeningHours().getOpenNow();

                    restaurantList.add(new Restaurant(result.getId(), result.getName(), result.getVicinity(), photoReference, rate, restaurantLocation, getDistance(userPosition, restaurantLocation), isOpened));
                }

                placesCache.put(userPosition, restaurantList);
                completion.onComplete(restaurantList);
            });
        }
    }

    private Integer getDistance(@Nullable LatLng StartP, LatLng EndP) {
        if (StartP == null) return null;
        int Radius = 6371;// radius of earth in Km (Type1, Type2) -> TypeR in {}
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
}