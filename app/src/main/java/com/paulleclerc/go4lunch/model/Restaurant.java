/*
 * Restaurant.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/27/20 5:13 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.model;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.BuildConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Serializable {
    public final String id;
    public final String name;
    public final String address;
    private final String photoReference;
    private final double lat;
    private final double lng;
    public final Integer distance;
    public final Rate rate;
    public final Boolean isOpened;
    private List<Workmate> interestedWorkmates;
    private RestaurantDetails details;

    public Restaurant(String id, String name, String address, String photoReference, Rate rate, LatLng location, Integer distance, Boolean isOpened, @Nullable List<Workmate> interestedWorkmates) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.photoReference = photoReference;
        this.lat = location.latitude;
        this.lng = location.longitude;
        this.rate = rate;
        this.distance = distance;
        this.isOpened = isOpened;

        if (interestedWorkmates != null) this.interestedWorkmates = interestedWorkmates;
        else this.interestedWorkmates = new ArrayList<>();
    }

    public String getPhotoUrl() {
        return photoReference == null ? null : "https://maps.googleapis.com/maps/api/place/photo?key=" + BuildConfig.GOOGLE_MAPS_AND_PLACES_KEY + "&maxheight=3000&photoreference=" + photoReference;
    }

    public LatLng getLocation() {
        return new LatLng(lat, lng);
    }

    public void setInterestedWorkmates(List<Workmate> workmates) {
        interestedWorkmates = workmates;
    }

    public List<Workmate> getInterestedWorkmates() {
        return interestedWorkmates;
    }

    public void setDetails(RestaurantDetails details) {
        this.details = details;
    }

    public RestaurantDetails getDetails() {
        return this.details;
    }

    public enum Rate {
        GOOD,
        MEDIUM,
        BAD,
        UNKNOWN
    }

    public static class RestaurantDetails {
        private final String phone;
        private final String website;

        public RestaurantDetails(String phone, String website) {
            this.phone = phone;
            this.website = website;
        }

        public String getPhone() {
            return phone;
        }

        public String getWebsite() {
            return website;
        }
    }
}
