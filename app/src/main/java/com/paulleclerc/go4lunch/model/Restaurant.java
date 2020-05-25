package com.paulleclerc.go4lunch.model;

import androidx.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.BuildConfig;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    public final String id;
    public final String name;
    public final String address;
    private final String photoReference;
    public final LatLng location;
    public final Integer distance;
    public final Rate rate;
    public final Boolean isOpened;
    private List<Workmate> interestedWorkmates;

    public Restaurant(String id, String name, String address, String photoReference, Rate rate, LatLng location, Integer distance, Boolean isOpened, @Nullable List<Workmate> interestedWorkmates) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.photoReference = photoReference;
        this.location = location;
        this.rate = rate;
        this.distance = distance;
        this.isOpened = isOpened;

        if (interestedWorkmates != null) this.interestedWorkmates = interestedWorkmates;
        else this.interestedWorkmates = new ArrayList<>();
    }

    public String getPhotoUrl() {
        return photoReference == null ? null : "https://maps.googleapis.com/maps/api/place/photo?key=" + BuildConfig.GOOGLE_MAPS_AND_PLACES_KEY + "&maxheight=500&photoreference=" + photoReference;
    }

    public void setInterestedWorkmates(List<Workmate> workmates) {
        interestedWorkmates = workmates;
    }

    public List<Workmate> getInterestedWorkmates() {
        return interestedWorkmates;
    }

    public enum Rate {
        GOOD,
        MEDIUM,
        BAD,
        UNKNOWN
    }
}
