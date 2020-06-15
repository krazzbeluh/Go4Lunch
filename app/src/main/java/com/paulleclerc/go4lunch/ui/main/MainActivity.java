/*
 * MainActivity.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/15/20 6:10 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.paulleclerc.go4lunch.BuildConfig;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.ui.main.fragments.DisplayRestaurantsInterface;
import com.paulleclerc.go4lunch.ui.main.fragments.list.RestaurantListFragment;
import com.paulleclerc.go4lunch.ui.main.fragments.map.MapFragment;
import com.paulleclerc.go4lunch.ui.main.fragments.workmates.WorkmatesFragment;
import com.paulleclerc.go4lunch.ui.restaurant_detail.RestaurantDetailActivity;
import com.paulleclerc.go4lunch.ui.settings.SettingsActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener, ShowDetailListener {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 358;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.main_nav_view)
    NavigationView navigationView;

    TextView usernameTextView;
    TextView userEmailTextView;
    ImageView userAvatar;

    MenuItem searchMenuItem;

    private MainViewModel viewModel;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Places.initialize(this, BuildConfig.GOOGLE_MAPS_AND_PLACES_KEY);

        setSupportActionBar(toolbar);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.isUserSignedIn().observe(this, isUserSignedIn -> {
            if (!isUserSignedIn) finish();
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.dark_white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.main_bottom_navigation_map);

        View headerLayout = navigationView.getHeaderView(0);
        userAvatar = headerLayout.findViewById(R.id.main_menu_user_avatar);
        userEmailTextView = headerLayout.findViewById(R.id.nav_view_user_email_textview);
        usernameTextView = headerLayout.findViewById(R.id.nav_view_username_textview);

        viewModel.getUserEmail().observe(this, userEmailTextView::setText);
        viewModel.getUsername().observe(this, usernameTextView::setText);
        viewModel.getUserAvatar().observe(this, avatarUrl -> Glide.with(this)
                .load(avatarUrl)
                .placeholder(R.drawable.workmate)
                .into(userAvatar));

        viewModel.getChosenRestaurant().observe(this, restaurant -> {
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.lateral_menu_lunch);
            menuItem.setVisible(restaurant != null);
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_bottom_navigation_map:
                openFragment(MapFragment.getInstance(this));
                if (searchMenuItem != null) searchMenuItem.setVisible(true);
                return true;
            case R.id.main_bottom_navigation_list:
                openFragment(RestaurantListFragment.getInstance(this));
                if (searchMenuItem != null) searchMenuItem.setVisible(true);
                return true;
            case R.id.main_bottom_navigation_workmates:
                if (searchMenuItem != null) searchMenuItem.setVisible(false);
                openFragment(WorkmatesFragment.getInstance());
                return true;
            case R.id.lateral_menu_lunch:
                Restaurant restaurant = viewModel.getChosenRestaurant().getValue();
                if (restaurant != null) {
                    Intent intent = new Intent(this, RestaurantDetailActivity.class);
                    intent.putExtra(RestaurantDetailActivity.KEY_RESTAURANT_EXTRA_SERIALIZABLE, restaurant);
                    startActivity(intent);
                }
                return false;
            case R.id.lateral_menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return false;
            case R.id.lateral_menu_log_out:
                viewModel.logOut();
                return false;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            showAutoComplete();
            return false;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            ((DisplayRestaurantsInterface) currentFragment).addPlace(place);
        }
    }

    private void showAutoComplete() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.RATING, Place.Field.OPENING_HOURS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    private void openFragment(Fragment fragment) {
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchMenuItem = menu.findItem(R.id.menu_search);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void showDetail(Restaurant restaurant) {
        Intent intent = new Intent(this, RestaurantDetailActivity.class);
        intent.putExtra(RestaurantDetailActivity.KEY_RESTAURANT_EXTRA_SERIALIZABLE, restaurant);
        startActivity(intent);
    }
}