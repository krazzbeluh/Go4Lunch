/*
 * Restaurant.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/15/20 6:10 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.model;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.BuildConfig;
import com.paulleclerc.go4lunch.network.restaurant_detail_response.Result;

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
    public final Rate rate;
    public final Boolean isOpened;
    private List<Workmate> interestedWorkmates;
    private RestaurantDetails details;

    public Restaurant(String id, String name, String address, String photoReference, Double rate, LatLng location, Boolean isOpened, @Nullable List<Workmate> interestedWorkmates) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.photoReference = photoReference;
        this.lat = location.latitude;
        this.lng = location.longitude;
        this.rate = getRateFromPlaceRating(rate);
        this.isOpened = isOpened;

        if (interestedWorkmates != null) this.interestedWorkmates = interestedWorkmates;
        else this.interestedWorkmates = new ArrayList<>();
    }

    public Restaurant(String placeId, Result result, @Nullable List<Workmate> interestedWorkmates) {
        this.id = placeId;
        this.name = result.getName();
        this.address = result.getVicinity();
        if (result.getPhotos() == null) photoReference = null;
        else photoReference = result.getPhotos().get(0).getPhotoReference();
        this.lat = result.getGeometry().getLocation().getLat();
        this.lng = result.getGeometry().getLocation().getLng();
        this.rate = getRateFromPlaceRating(result.getRating());
        this.isOpened = result.getOpeningHours().getOpenNow();

        this.details = new RestaurantDetails(result.getFormattedPhoneNumber(), result.getWebsite());

        if (interestedWorkmates != null) this.interestedWorkmates = interestedWorkmates;
        else this.interestedWorkmates = new ArrayList<>();
    }

    private Rate getRateFromPlaceRating(Double rating) {
        Rate rate;
        if (rating == null) {
            rate = Restaurant.Rate.UNKNOWN;
        } else {
            rating = rating / 5 * 3;

            if (rating < 1) {
                rate = Restaurant.Rate.BAD;
            } else if (rating < 2) {
                rate = Restaurant.Rate.MEDIUM;
            } else {
                rate = Restaurant.Rate.GOOD;
            }
        }

        return rate;
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

    public Integer getDistance(LatLng location) {
        LatLng selfLocation = getLocation();
        if (selfLocation == null) return null;
        int Radius = 6371;// radius of earth in Km (Type1, Type2) -> TypeR in {}
        double lat1 = selfLocation.latitude;
        double lat2 = location.latitude;
        double lon1 = selfLocation.longitude;
        double lon2 = location.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return (int) (Radius * c * 1000);
    }

    public static class RestaurantDetails implements Serializable {
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
