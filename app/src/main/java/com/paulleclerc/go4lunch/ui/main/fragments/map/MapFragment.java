package com.paulleclerc.go4lunch.ui.main.fragments.map;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.ui.main.MainActivity;
import com.paulleclerc.go4lunch.ui.main.ShowDetailListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private static final String PERM = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int GET_LOCATION_PERMS = 100;
    private static final long MIN_TIME = 400;
    private static final long MIN_DISTANCE = 300;
    private static final String TAG = MapFragment.class.getSimpleName();
    private static MapFragment INSTANCE;

    @BindView(R.id.map)
    MapView mapView;

    private final ShowDetailListener showDetailListener;
    private MapViewModel viewModel;
    private LocationManager locationManager;
    private GoogleMap map;
    private final List<Marker> markers = new ArrayList<>();

    public MapFragment(ShowDetailListener showDetailListener) {
        this.showDetailListener = showDetailListener;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment getInstance(ShowDetailListener showDetailListener) {
        if (INSTANCE == null) INSTANCE = new MapFragment(showDetailListener);
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
        
        viewModel = new ViewModelProvider(this).get(MapViewModel.class);

        requiresAccessLocationPermission();

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
        if (mapView != null) mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
         if (mapView != null) mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
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

    /*
        Permissions management
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(GET_LOCATION_PERMS)
    private void requiresAccessLocationPermission() {
        if (EasyPermissions.hasPermissions(Objects.requireNonNull(getContext()), PERM)) {
            locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
            assert locationManager != null;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_location_message), GET_LOCATION_PERMS, PERM);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);

        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(Objects.requireNonNull(getContext()), R.raw.google_map_style));
        mapView.onResume();

        viewModel.getPlaces().observe(this, this::displayRestaurants);
        moveCamera();
    }

    void displayRestaurants(@NonNull List<Restaurant> restaurants) {
        for (Marker marker: markers) {
            marker.remove();
        }

        for (Restaurant restaurant: restaurants) {
            LatLng location = restaurant.getLocation();
            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng(location.latitude, location.longitude))
                    .icon(BitmapDescriptorFactory.fromResource((restaurant.getInterestedWorkmates().size() == 0) ? R.drawable.marker_restaurant_orange : R.drawable.marker_restaurant_green))
                    .title(restaurant.name);
            markers.add(map.addMarker(marker));
        }
    }

    private LatLng position;
    private void moveCamera() {
        if (map != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        viewModel.fetchPlaces(position);
        this.position = new LatLng(location.getLatitude(), location.getLongitude());
        moveCamera();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
