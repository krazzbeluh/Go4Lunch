package com.paulleclerc.go4lunch.main;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paulleclerc.go4lunch.main.fragments.MapFragment;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.main.fragments.RestaurantListFragment;
import com.paulleclerc.go4lunch.main.fragments.WorkmatesFragment;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, LocationListener {
    private static final String PERM = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int GET_LOCATION_PERMS = 100;
    private static final long MIN_TIME = 400;
    private static final long MIN_DISTANCE = 1000;

    private MainViewModel viewModel;
    private LocationManager locationManager;

    @BindView(R.id.main_bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    /*
        BottomNavBar actions
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_bottom_navigation_map:
                openFragment(MapFragment.getInstance(MapStyleOptions.loadRawResourceStyle(this, R.raw.google_map_style), viewModel.places));
                return true;
            case R.id.main_bottom_navigation_list:
                openFragment(RestaurantListFragment.newInstance());
                return true;
            case R.id.main_bottom_navigation_workmates:
                openFragment(WorkmatesFragment.newInstance());
                return true;
        }
        return false;
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
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
        if (EasyPermissions.hasPermissions(this, PERM)) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            assert locationManager != null;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_location_message), GET_LOCATION_PERMS, PERM);
        }
    }

    /*
        LocationListener
     */

    @Override
    public void onLocationChanged(Location location) {
        viewModel.fetchPlacesForLocation(location);

        viewModel.places.observe(this, places -> {
            // TODO: Ask to Nicolas
        });

        locationManager.removeUpdates(this);
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
