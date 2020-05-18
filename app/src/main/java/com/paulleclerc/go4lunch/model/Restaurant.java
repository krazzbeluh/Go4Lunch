package com.paulleclerc.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;

public class Restaurant {
    public final String id;
    public final String name;
    public final String address;
    public final String photoReference;
    public final LatLng location;
    public final Integer distance;
    public final Rate rate;

    public Restaurant(String id, String name, String address, String photoReference, Rate rate, LatLng location, Integer distance) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.photoReference = photoReference;
        this.location = location;
        this.rate = rate;
        this.distance = distance;
    }

    public enum Rate {
        GOOD,
        MEDIUM,
        BAD,
        UNKNOWN
    }
}
