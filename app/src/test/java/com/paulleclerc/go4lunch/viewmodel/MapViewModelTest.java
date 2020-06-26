/*
 * MapViewModelTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 9:42 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.ui.main.fragments.map.MapViewModel;
import com.paulleclerc.go4lunch.viewmodel.mock.PlacesRepositoryFake;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class MapViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    MapViewModel viewModel;

    @Mock
    Application application;
    PlacesRepositoryFake placesRepository;

    Restaurant restaurantForTests = new Restaurant("go4lunch", "go4lunch", "go4lunch", "go4lunch"
            , 0.0, new LatLng(0.0, 0.0), true, null);

    @Before
    public void setUp() {
        placesRepository = new PlacesRepositoryFake();
        viewModel = new MapViewModel(application, placesRepository);
    }

    @Test
    public void testGetPlacesShouldContainNullIfNoActions() {
        assertNull(viewModel.getPlaces().getValue());
    }

    @Test
    public void testGetPlacesShouldReturnValuesIfSetPlacesCalled() {
        assertNull(viewModel.getPlaces().getValue());
        viewModel.setPlaces(new ArrayList<>());
        assertNotNull(viewModel.getPlaces().getValue());
    }

    @Test
    public void testGetPlaceDetailShouldResponseShouldContainRestaurantSentToCallback() {
        placesRepository.addRestaurantToQueue(restaurantForTests);

        LiveData<Restaurant> restaurant = viewModel.getPlaceDetail("");

        assertEquals(restaurant.getValue(), restaurantForTests);
    }

    @Test
    public void testFetchPlacesShouldStoreListSentToCallback() {
        List<Restaurant> restaurants = Collections.singletonList(restaurantForTests);
        placesRepository.addRestaurantsListToQueue(restaurants);

        viewModel.fetchPlaces(new LatLng(0.0, 0.0));

        assertEquals(viewModel.getPlaces().getValue(), restaurants);
    }
}
