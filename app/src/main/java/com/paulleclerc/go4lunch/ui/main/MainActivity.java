/*
 * MainActivity.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/8/20 10:44 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.ui.main.fragments.list.RestaurantListFragment;
import com.paulleclerc.go4lunch.ui.main.fragments.map.MapFragment;
import com.paulleclerc.go4lunch.ui.main.fragments.workmates.WorkmatesFragment;
import com.paulleclerc.go4lunch.ui.restaurant_detail.RestaurantDetailActivity;
import com.paulleclerc.go4lunch.ui.settings.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener, ShowDetailListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.main_nav_view)
    NavigationView navigationView;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_bottom_navigation_map:
                openFragment(MapFragment.getInstance(this));
                return true;
            case R.id.main_bottom_navigation_list:
                openFragment(RestaurantListFragment.getInstance(this));
                return true;
            case R.id.main_bottom_navigation_workmates:
                openFragment(WorkmatesFragment.getInstance());
                return true;
            case R.id.lateral_menu_lunch:
                Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
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

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showDetail(Restaurant restaurant) {
        Intent intent = new Intent(this, RestaurantDetailActivity.class);
        intent.putExtra(getString(R.string.restaurant_extra_identifier), restaurant);
        startActivity(intent);
    }
}