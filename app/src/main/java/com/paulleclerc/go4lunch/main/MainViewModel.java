package com.paulleclerc.go4lunch.main;

import android.app.Application;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResult;
import com.paulleclerc.go4lunch.repository.LocationRepository;
import com.paulleclerc.go4lunch.repository.PlacesRepository;

public class MainViewModel extends AndroidViewModel {
    MutableLiveData<PlacesSearchResult[]> places = new MutableLiveData<>();

    private final PlacesRepository placesRepository = new PlacesRepository();
    private final LocationRepository locationRepository = new LocationRepository();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    void fetchPlacesForLocation(Location location) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        placesRepository.fetchPlaces(position, places -> this.places.setValue(places));
    }
}
