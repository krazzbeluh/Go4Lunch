package com.paulleclerc.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.BuildConfig;

public class Restaurant {
    public final String id;
    public final String name;
    public final String address;
    private final String photoReference;
    public final LatLng location;
    public final Integer distance;
    public final Rate rate;
    public final Boolean isOpened;

    public Restaurant(String id, String name, String address, String photoReference, Rate rate, LatLng location, Integer distance, Boolean isOpened) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.photoReference = photoReference;
        this.location = location;
        this.rate = rate;
        this.distance = distance;
        this.isOpened = isOpened;
    }

    public String getPhotoUrl() {
        return photoReference == null ? null : "https://maps.googleapis.com/maps/api/place/photo?key=" + BuildConfig.GOOGLE_MAPS_AND_PLACES_KEY + "&maxheight=500&photoreference=" + photoReference;
    }

    public enum Rate {
        GOOD,
        MEDIUM,
        BAD,
        UNKNOWN
    }
}
