package com.paulleclerc.go4lunch.ui.main.fragments.map;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.repository.PlacesRepository;

import java.util.List;

public class MapViewModel extends AndroidViewModel {
    private final PlacesRepository placesRepository = new PlacesRepository(getApplication().getApplicationContext());

    private final MutableLiveData<List<Restaurant>> places = new MutableLiveData<>();

    public MapViewModel(@NonNull Application application) {
        super(application);
    }

    void fetchPlaces(LatLng location) {
        placesRepository.fetchPlaces(location, this.places::setValue);
    }

    LiveData<List<Restaurant>> getPlaces() {
        return places;
    }
}
