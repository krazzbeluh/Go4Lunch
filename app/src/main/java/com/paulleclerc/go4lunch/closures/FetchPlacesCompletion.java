package com.paulleclerc.go4lunch.closures;

import com.paulleclerc.go4lunch.model.Restaurant;

import java.util.List;

public interface FetchPlacesCompletion {
    void onComplete(List<Restaurant> restaurants);
}
