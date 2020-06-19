/*
 * UserRepositoryTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/19/20 4:34 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.InstanceIdResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {
    UserRepository userRepository;

    @Mock
    AuthRepository auth;
    @Mock
    FirestoreRepository firestore;
    @Mock
    FirStorageRepository storage;
    @Mock
    PlacesRepository placesRepository;

    @Mock
    Task<InstanceIdResult> task;
    @Mock
    InstanceIdResult result;


    @Before
    public void setUp() {
        userRepository = new UserRepository(auth, firestore, storage, placesRepository);
    }

    @Test
    public void testGetTokenShouldReturnNullIfError() {
        when(task.getResult()).thenReturn(result);
        when(task.isSuccessful()).thenReturn(false);
        when(task.getException()).thenReturn(new Exception("Erreur"));
        assertNull(userRepository.getToken(task));
    }


    @Test
    public void testGetTokenShouldReturnNullIfResultNull() {
        when(task.getResult()).thenReturn(null);
        when(task.isSuccessful()).thenReturn(true);
        when(task.getException()).thenReturn(new Exception("Erreur"));
        assertNull(userRepository.getToken(task));
    }

    @Test
    public void testGetTokenShouldReturnStringIfResultNotNull() {
        String response = "Go4LunchFCMToken";

        when(task.getResult()).thenReturn(result);
        when(task.isSuccessful()).thenReturn(true);
        when(result.getToken()).thenReturn(response);
        assertEquals(userRepository.getToken(task), response);
    }
}
