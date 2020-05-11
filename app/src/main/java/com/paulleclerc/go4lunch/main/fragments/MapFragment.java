package com.paulleclerc.go4lunch.main.fragments;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.MutableLiveData;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.model.PlacesSearchResult;
import com.paulleclerc.go4lunch.R;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = MapFragment.class.getSimpleName();
    private static MapFragment INSTANCE;

    @BindView(R.id.map)
    MapView mapView;

    private final MapStyleOptions mapStyleOptions;
    private GoogleMap map;

    public MapFragment(MapStyleOptions mapStyleOptions, MutableLiveData<PlacesSearchResult[]> places) {
        this.mapStyleOptions = mapStyleOptions;
        // Required empty public constructor
        places.observe(this, this::displayRestaurants);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment getInstance(MapStyleOptions mapStyleOptions, MutableLiveData<PlacesSearchResult[]> places) {
        if (INSTANCE == null) INSTANCE = new MapFragment(mapStyleOptions, places);
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        mapView.onCreate(savedInstanceState);

        try {
            MapsInitializer.initialize(Objects.requireNonNull(this.getContext()));
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        }

        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        //mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMinZoomPreference(12);

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(48.8568, 2.3511)));

        map.setMapStyle(mapStyleOptions);
        mapView.onResume();
    }

    void displayRestaurants(PlacesSearchResult[] restaurants) {
        for (PlacesSearchResult restaurant: restaurants) {
            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng(restaurant.geometry.location.lat, restaurant.geometry.location.lng))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_restaurant_orange))
                    .title(restaurant.name);
            map.addMarker(marker);
        }
    }
}
