/*
 * MainActivity.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/27/20 5:13 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.ui.main.fragments.list.RestaurantListFragment;
import com.paulleclerc.go4lunch.ui.main.fragments.map.MapFragment;
import com.paulleclerc.go4lunch.ui.main.fragments.workmates.WorkmatesFragment;
import com.paulleclerc.go4lunch.ui.restaurant_detail.RestaurantDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ShowDetailListener {
    @BindView(R.id.main_bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
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