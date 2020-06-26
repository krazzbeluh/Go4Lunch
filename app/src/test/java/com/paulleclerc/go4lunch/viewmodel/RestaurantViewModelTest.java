/*
 * RestaurantViewModelTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 4:06 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.ui.restaurant_detail.RestaurantDetailViewModel;
import com.paulleclerc.go4lunch.viewmodel.mock.FirestoreRepositoryFake;
import com.paulleclerc.go4lunch.viewmodel.mock.PlacesRepositoryFake;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantViewModelTest {
    static Restaurant restaurantForTests;
    static Restaurant.RestaurantDetails details;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    RestaurantDetailViewModel viewModel;
    @Mock
    Application application;
    PlacesRepositoryFake placesRepository;
    FirestoreRepositoryFake firestoreRepository;

    @BeforeClass
    public static void setupClass() {
        restaurantForTests = new Restaurant("go4lunch", "go4lunch", "go4lunch", "go4lunch"
                , 0.0, new LatLng(0.0, 0.0), true, null);
        details = new Restaurant.RestaurantDetails("0123456789", "https://www" +
                ".go4lunch.fr");
    }

    @Before
    public void setUp() {
        placesRepository = new PlacesRepositoryFake();
        firestoreRepository = new FirestoreRepositoryFake();
        viewModel = new RestaurantDetailViewModel(application, placesRepository,
                firestoreRepository);
    }

    @Test
    public void testGetPlaceDetailShouldReturnDetailSentToCallback() {
        final Restaurant restaurant = restaurantForTests;
        restaurant.setDetails(details);
        placesRepository.addRestaurantToQueue(restaurant);

        assertEquals(details, viewModel.getPlaceDetail(restaurantForTests).getValue());
    }

    @Test
    public void testGetIsLikedShouldReturnNullIfNullInCallback() {
        placesRepository.addBoolToQueue(null);
        assertNull(viewModel.getIsLiked(restaurantForTests).getValue());
    }

    @Test
    public void testGetIsLikedShouldReturnTrueIfTrueInCallback() {
        placesRepository.addBoolToQueue(true);
        assertTrue(viewModel.getIsLiked(restaurantForTests).getValue());
    }

    @Test
    public void testGetIsLikedShouldReturnFalseIfFalseInCallback() {
        placesRepository.addBoolToQueue(false);
        assertFalse(viewModel.getIsLiked(restaurantForTests).getValue());
    }

    @Test
    public void testGetChosenRestaurantIdShouldReturnStringSentInCallback() {
        String placeId = "chosenRestaurantId";

        firestoreRepository.addStringToQueue(placeId);

        assertEquals(placeId, viewModel.getChosenRestaurantId().getValue());
    }

    @Test
    public void testChooseRestaurantShouldNotSetAlertMessageValueIfSuccess() {
        firestoreRepository.setShouldFail(false);
        viewModel.chooseRestaurant(restaurantForTests);
        assertNull(viewModel.getAlertMessage().getValue());
    }

    @Test
    public void testChooseRestaurantShouldNotSetAlertMessageValueIfFailure() {
        String message = "impossibleOperationAlertMessage";

        Context context = mock(Context.class);
        when(application.getApplicationContext()).thenReturn(context);
        when(context.getString(R.string.impossible_operation)).thenReturn(message);

        firestoreRepository.setShouldFail(true);
        viewModel.chooseRestaurant(restaurantForTests);
        assertEquals(message, viewModel.getAlertMessage().getValue());
    }

    @Test
    public void testSwitchLikeShouldNotDoAnythingIfIsLikedIsNull() {
        viewModel.switchLike(restaurantForTests);

        placesRepository.addBoolToQueue(null);
        viewModel.getIsLiked(restaurantForTests);

        assertNull(placesRepository.getDislikedPlace());
        assertNull(placesRepository.getLikedPlace());
    }

    @Test
    public void testSwitchLikeShouldCallLikePlaceIfIsLikedIsFalse() {
        viewModel.switchLike(restaurantForTests);

        placesRepository.addBoolToQueue(false);
        viewModel.getIsLiked(restaurantForTests);

        viewModel.switchLike(restaurantForTests);

        assertNull(placesRepository.getDislikedPlace());
        assertEquals(restaurantForTests.id, placesRepository.getLikedPlace());
    }

    @Test
    public void testSwitchLikeShouldCallDisLikePlaceIfIsLikedIsTrue() {
        viewModel.switchLike(restaurantForTests);

        placesRepository.addBoolToQueue(true);
        viewModel.getIsLiked(restaurantForTests);

        viewModel.switchLike(restaurantForTests);

        assertEquals(restaurantForTests.id, placesRepository.getDislikedPlace());
        assertNull(placesRepository.getLikedPlace());
    }
}
