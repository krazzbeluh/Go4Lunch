package com.paulleclerc.go4lunch.ui.main.fragments.list;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantListFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantListFragment extends Fragment implements LocationListener {
    private static final String PERM = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int GET_LOCATION_PERMS = 100;
    private static final long MIN_TIME = 400;
    private static final long MIN_DISTANCE = 300;

    private RestaurantListViewModel viewModel;
    private LocationManager locationManager;
    private LinearLayoutManager linearLayoutManager;
    private RestaurantListAdapter adapter;

    @BindView(R.id.restaurant_recyclerview)
    RecyclerView recyclerView;

    public RestaurantListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RestaurantListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantListFragment getInstance() {
        return new RestaurantListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        linearLayoutManager = new LinearLayoutManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RestaurantListAdapter();
        viewModel = new ViewModelProvider(this).get(RestaurantListViewModel.class);
        viewModel.getPlaces().observe(getViewLifecycleOwner(), adapter::setPlaces);

        recyclerView.setAdapter(adapter);

        requiresAccessLocationPermission();

        return view;
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        viewModel.fetchPlaces(position);
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
}
