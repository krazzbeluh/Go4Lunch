/*
 * WorkmatesViewModelTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 10:55 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.model.Workmate;
import com.paulleclerc.go4lunch.ui.main.fragments.workmates.WorkmatesViewModel;
import com.paulleclerc.go4lunch.viewmodel.mock.PlacesRepositoryFake;
import com.paulleclerc.go4lunch.viewmodel.mock.WorkmatesRepositoryFake;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class WorkmatesViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    WorkmatesViewModel viewModel;

    @Mock
    Application application;

    WorkmatesRepositoryFake workmatesRepository;
    PlacesRepositoryFake placesRepository;

    Restaurant restaurantForTests = new Restaurant("go4lunch", "go4lunch", "go4lunch", "go4lunch"
            , 0.0, new LatLng(0.0, 0.0), true, null);
    Workmate workmateForTests = new Workmate("uid", "username", "avatarUri", "documentID",
            "chosenRestaurantId", "chosenRestaurantName");

    @Before
    public void setUp() {
        placesRepository = new PlacesRepositoryFake();
        workmatesRepository = new WorkmatesRepositoryFake();
        viewModel = new WorkmatesViewModel(application, workmatesRepository, placesRepository);
    }

    @Test
    public void testFetchWorkmatesShouldStoreWorkmatesSentToCallback() {
        List<Workmate> workmates = Collections.singletonList(workmateForTests);
        workmatesRepository.addWorkmatesToQueue(workmates);

        assertNull(viewModel.getWorkmates().getValue());

        viewModel.fetchWorkmates();

        assertEquals(workmates, viewModel.getWorkmates().getValue());
    }

    @Test
    public void testFetchRestaurantShouldReturnRestaurantSentToCallback() {
        placesRepository.addRestaurantToQueue(restaurantForTests);

        assertEquals(restaurantForTests, viewModel.fetchRestaurant("").getValue());
    }
}
