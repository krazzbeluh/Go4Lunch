package com.paulleclerc.go4lunch.main.fragments.list;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResult;
import com.paulleclerc.go4lunch.repository.PlacesRepository;

public class RestaurantListViewModel extends AndroidViewModel {
    private final PlacesRepository placesRepository = new PlacesRepository();

    private final MutableLiveData<PlacesSearchResult[]> places = new MutableLiveData<>();

    public RestaurantListViewModel(@NonNull Application application) {
        super(application);
    }

    void fetchPlaces(LatLng location) {
        placesRepository.fetchPlaces(location, this.places::setValue);
    }

    public LiveData<PlacesSearchResult[]> getPlaces() {
        return places;
    }
}
