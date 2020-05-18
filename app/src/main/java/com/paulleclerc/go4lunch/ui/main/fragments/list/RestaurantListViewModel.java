package com.paulleclerc.go4lunch.ui.main.fragments.list;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.repository.PlacesRepository;

import java.util.List;

public class RestaurantListViewModel extends AndroidViewModel {
    private final PlacesRepository placesRepository = new PlacesRepository(Places.createClient(getApplication().getApplicationContext()));

    private final MutableLiveData<List<Restaurant>> places = new MutableLiveData<>();

    public RestaurantListViewModel(@NonNull Application application) {
        super(application);
    }

    void fetchPlaces(LatLng location) {
        placesRepository.fetchPlaces(location, this.places::setValue);
    }

    public LiveData<List<Restaurant>> getPlaces() {
        return places;
    }
}
