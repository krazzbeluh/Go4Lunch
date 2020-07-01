/*
 * MainViewModelTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 10:16 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.repository.AuthRepository;
import com.paulleclerc.go4lunch.ui.main.MainViewModel;
import com.paulleclerc.go4lunch.viewmodel.mock.FirestoreRepositoryFake;
import com.paulleclerc.go4lunch.viewmodel.mock.UserRepositoryFake;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    MainViewModel viewModel;

    @Mock
    Application application;
    @Mock
    AuthRepository authRepository;
    UserRepositoryFake userRepository;
    FirestoreRepositoryFake firestoreRepository;

    Restaurant restaurantForTests = new Restaurant("go4lunch", "go4lunch", "go4lunch", "go4lunch"
            , 0.0, new LatLng(0.0, 0.0), true, null);

    @Before
    public void setUp() {
        userRepository = new UserRepositoryFake();
        firestoreRepository = new FirestoreRepositoryFake();
        viewModel = new MainViewModel(application, authRepository, userRepository,
                firestoreRepository);
    }

    @Test
    public void testLogOutShouldSetIsUserSignedInValueToFalse() {
        when(authRepository.signOut()).thenReturn(true);

        LiveData<Boolean> isUserSignedIn = viewModel.isUserSignedIn();
        assertNull(isUserSignedIn.getValue());
        viewModel.logOut();
        assertFalse(isUserSignedIn.getValue());
    }

    @Test
    public void testGetChosenRestaurantShouldReturnRestaurantGivenInCallback() {
        userRepository.addRestaurantToQueue(restaurantForTests);

        assertEquals(viewModel.getChosenRestaurant().getValue(), restaurantForTests);
    }

    @Test
    public void testGetUsernameShouldReturnStringSentToCallback() {
        String username = "Go4LunchUsername";
        firestoreRepository.addStringToQueue(username);

        assertEquals(viewModel.getUsername().getValue(), username);
    }

    @Test
    public void testGetUserEmailShouldReturnEmailSentByAuth() {
        String email = "UTest@Go4Lunch.fr";
        when(authRepository.getEmail()).thenReturn(email);

        assertEquals(email, viewModel.getUserEmail().getValue());
    }

    @Test
    public void testGetUserAvatarShouldReturnStringSentToCallback() {
        String url = "https://go4Lunch.fr/user/avatar/3794";
        userRepository.addStringToQueue(url);

        assertEquals(url, viewModel.getUserAvatar().getValue());
    }
}
