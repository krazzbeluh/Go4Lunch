package com.paulleclerc.go4lunch.repository;

import androidx.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.closures.FetchPlacesCompletion;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.network.PlaceClient;

import java.util.*;

public class PlacesRepository {
    private static final Map<LatLng, List<Restaurant>> placesCache = new HashMap<>();

    private static final String TAG = PlacesRepository.class.getSimpleName();

    public PlacesRepository() {

    }

    public void fetchPlaces(LatLng position, FetchPlacesCompletion completion) {
        PlaceClient client = new PlaceClient();
        client.fetchRestaurants();
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

    private interface FetchDetailsCallback {
        void onComplete(List<Restaurant> restaurants);
    }
}