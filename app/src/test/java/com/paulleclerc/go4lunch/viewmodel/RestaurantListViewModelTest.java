/*
 * RestaurantListViewModelTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 10:32 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.ui.main.fragments.list.RestaurantListViewModel;
import com.paulleclerc.go4lunch.viewmodel.mock.PlacesRepositoryFake;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantListViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    RestaurantListViewModel viewModel;

    @Mock
    Application application;
    PlacesRepositoryFake placesRepository;

    Restaurant restaurantForTests = new Restaurant("go4lunch", "go4lunch", "go4lunch", "go4lunch"
            , 0.0, new LatLng(0.0, 0.0), true, null);

    @Before
    public void setUp() {
        placesRepository = new PlacesRepositoryFake();
        viewModel = new RestaurantListViewModel(application, placesRepository);
    }

    @Test
    public void testFetchPlacesShouldStorePlacesSentToCallback() {
        List<Restaurant> restaurants = Collections.singletonList(restaurantForTests);
        placesRepository.addRestaurantsListToQueue(restaurants);

        viewModel.fetchPlaces(null);

        assertEquals(restaurants, viewModel.getPlaces().getValue());
    }

    @Test
    public void testGetPlaceDetailShouldReturnRestaurantsSentToCallback() {
        placesRepository.addRestaurantToQueue(restaurantForTests);

        assertEquals(restaurantForTests, viewModel.getPlaceDetail("").getValue());
    }
}
