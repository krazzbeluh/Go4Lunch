package com.paulleclerc.go4lunch.closures;

import com.google.maps.model.PlacesSearchResult;

public interface FetchPlacesCompletion {
    void onComplete(PlacesSearchResult[] places);
}
