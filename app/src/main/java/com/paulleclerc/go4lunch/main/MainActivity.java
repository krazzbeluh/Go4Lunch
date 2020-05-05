package com.paulleclerc.go4lunch.main;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.ButterKnife;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.paulleclerc.go4lunch.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String PERM = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int GET_LOCATION_PERMS = 100;
    private static final long MIN_TIME = 400;
    private static final long MIN_DISTANCE = 1000;

    private GoogleMap map;
    private LocationManager locationManager;
    private LatLng position;
    private com.google.maps.model.LatLng convertLatLng(@NonNull LatLng latLng) {
        return new com.google.maps.model.LatLng(latLng.latitude, latLng.longitude);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.google_map_style));
        requiresAccessLocationPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(GET_LOCATION_PERMS)
    private void requiresAccessLocationPermission() {
        if (EasyPermissions.hasPermissions(this, PERM)) {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
            map.setMyLocationEnabled(true);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_location_message), GET_LOCATION_PERMS, PERM);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        position = new LatLng(location.getLatitude(), location.getLongitude());
        searchForPlaces(position);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 17);
        map.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    private void searchForPlaces(LatLng position) {
        GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(getString(R.string.google_maps_and_places_key))
            .build();

        PlacesSearchResponse request  = new PlacesSearchResponse();

        try {
            request = PlacesApi.nearbySearchQuery(context, convertLatLng(position))
                    .rankby(RankBy.DISTANCE)
                    .type(PlaceType.RESTAURANT)
                    .await();
        } catch (Exception e) {
            Log.e(TAG, "requiresAccessLocationPermission: ", e);
        }

        PlacesSearchResult[] placesSearchResults = request.results;

        Log.d(TAG, "searchForPlaces: " + placesSearchResults.length);
        for (PlacesSearchResult result : placesSearchResults) {
            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng(result.geometry.location.lat, result.geometry.location.lng))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_restaurant_orange))
                    .title(result.name);
            map.addMarker(marker);
        }
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
