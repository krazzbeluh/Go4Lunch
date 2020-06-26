/*
 * SettingsViewModelTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 4:55 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.paulleclerc.go4lunch.repository.FirStorageRepository;
import com.paulleclerc.go4lunch.ui.settings.SettingsViewModel;
import com.paulleclerc.go4lunch.viewmodel.mock.AvatarRepositoryFake;
import com.paulleclerc.go4lunch.viewmodel.mock.FirestoreRepositoryFake;
import com.paulleclerc.go4lunch.viewmodel.mock.UserRepositoryFake;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SettingsViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    SettingsViewModel viewModel;

    @Mock
    Application application;
    @Mock
    FirStorageRepository storageRepository;
    UserRepositoryFake userRepository;
    FirestoreRepositoryFake firestoreRepository;
    AvatarRepositoryFake avatarRepository;


    @Before
    public void setUp() {
        userRepository = new UserRepositoryFake();
        firestoreRepository = new FirestoreRepositoryFake();
        avatarRepository = new AvatarRepositoryFake();

        viewModel = new SettingsViewModel(application, userRepository, storageRepository,
                firestoreRepository, avatarRepository);
    }

    @Test
    public void testGetAvatarShouldReturnBitmapIfSuccess() {
        Bitmap bmp = mock(Bitmap.class);
        userRepository.addStringToQueue("https://go4lunch/avatar/23");
        avatarRepository.setBitmapOutput(bmp);

        assertEquals(bmp, viewModel.getAvatar().getValue());
    }

    @Test
    public void testGetUsernameShouldReturnStringInCallback() {
        String username = "Go4LunchUsername";

        firestoreRepository.addStringToQueue(username);

        assertEquals(username, viewModel.getUsername().getValue());
    }

    @Test
    public void testGetAllowNotificationsShouldReturnBooleanInCallback() {
        firestoreRepository.addBooleanToQueue(true);
        assertTrue(viewModel.getAllowNotifications().getValue());
    }
}
