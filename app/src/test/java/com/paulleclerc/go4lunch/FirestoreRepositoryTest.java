/*
 * FirestoreRepositoryTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/18/20 3:55 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paulleclerc.go4lunch.repository.AuthRepository;
import com.paulleclerc.go4lunch.repository.FirStorageRepository;
import com.paulleclerc.go4lunch.repository.FirestoreRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.paulleclerc.go4lunch.repository.FirestoreRepository.KEY_ALLOW_NOTIFICATIONS;
import static com.paulleclerc.go4lunch.repository.FirestoreRepository.KEY_AVATAR_NAME;
import static com.paulleclerc.go4lunch.repository.FirestoreRepository.KEY_CHOSEN_PLACE_ID;
import static com.paulleclerc.go4lunch.repository.FirestoreRepository.KEY_USERNAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirestoreRepositoryTest {
    FirestoreRepository firestoreRepository;

    @Mock
    FirebaseFirestore db;
    @Mock
    AuthRepository auth;
    @Mock
    FirStorageRepository storage;

    @Mock
    DocumentSnapshot documentSnapshot;
    @Mock
    Task<DocumentSnapshot> task;

    @Before
    public void setUp() {
        firestoreRepository = new FirestoreRepository(db, auth, storage);
    }

    @Test
    public void testGetAvatarUriShouldReturnNullIfError() {
        assertNull(firestoreRepository.getAvatarUri(documentSnapshot, new Exception("Error")));
    }

    @Test
    public void testGetAvatarUriShouldReturnNullIfNoSnapshot() {
        assertNull(firestoreRepository.getAvatarUri(null, null));
    }

    @Test
    public void testGetAvatarUriShouldReturnStringIfNoError() {
        String url = "https://go4lunch.fr";
        when(documentSnapshot.getString(KEY_AVATAR_NAME)).thenReturn(url);
        assertEquals(firestoreRepository.getAvatarUri(documentSnapshot, null), url);
    }

    @Test
    public void testGetUsernameFromResultShouldReturnNullIfError() {
        when(task.getResult()).thenReturn(documentSnapshot);
        when(task.isSuccessful()).thenReturn(false);
        when(task.getException()).thenReturn(new Exception("Erreur"));
        assertNull(firestoreRepository.getUsernameFromResult(task));
    }

    @Test
    public void testGetUsernameFromResultShouldReturnNullIfNoSnapshot() {
        when(task.getResult()).thenReturn(null);
        when(task.isSuccessful()).thenReturn(true);
        assertNull(firestoreRepository.getUsernameFromResult(task));
    }

    @Test
    public void testGetUsernameFromResultShouldReturnStringIfNoError() {
        String response = "username";

        when(task.getResult()).thenReturn(documentSnapshot);
        when(task.isSuccessful()).thenReturn(true);
        when(documentSnapshot.getString(KEY_USERNAME)).thenReturn(response);
        assertEquals(firestoreRepository.getUsernameFromResult(task), response);
    }

    @Test
    public void testGetPlaceIdShouldReturnNullIfError() {
        assertNull(firestoreRepository.getPlaceId(documentSnapshot, new Exception("Erreur")));
    }

    @Test
    public void testGetPlaceIdShouldReturnNullIfNoSnapshot() {
        assertNull(firestoreRepository.getPlaceId(null, null));
    }

    @Test
    public void testGetPlaceIdShouldReturnStringIfNoError() {
        String response = "azertyuiop";

        when(documentSnapshot.getString(KEY_CHOSEN_PLACE_ID)).thenReturn(response);

        assertEquals(firestoreRepository.getPlaceId(documentSnapshot, null), response);
    }

    @Test
    public void testGetAreNotificationsAllowedShouldReturnNullIfError() {
        when(task.getResult()).thenReturn(documentSnapshot);
        when(task.isSuccessful()).thenReturn(false);
        assertNull(firestoreRepository.getAreNotificationsAllowed(task));
    }

    @Test
    public void testGetAreNotificationsAllowedShouldReturnNullIfNoSnapshot() {
        when(task.getResult()).thenReturn(null);
        when(task.isSuccessful()).thenReturn(true);
        assertNull(firestoreRepository.getAreNotificationsAllowed(task));
    }

    @Test
    public void testGetAreNotificationsAllowedShouldReturnTrueIfResponseNull() {
        when(task.getResult()).thenReturn(documentSnapshot);
        when(task.isSuccessful()).thenReturn(true);
        when(documentSnapshot.getBoolean(KEY_ALLOW_NOTIFICATIONS)).thenReturn(null);
        assertTrue(firestoreRepository.getAreNotificationsAllowed(task));
    }

    @Test
    public void testGetAreNotificationsAllowedShouldReturnTrueIfResponseTrue() {
        when(task.getResult()).thenReturn(documentSnapshot);
        when(task.isSuccessful()).thenReturn(true);
        when(documentSnapshot.getBoolean(KEY_ALLOW_NOTIFICATIONS)).thenReturn(true);
        assertTrue(firestoreRepository.getAreNotificationsAllowed(task));
    }

    @Test
    public void testGetAreNotificationsAllowedShouldReturnFalseIfResponseFalse() {
        when(task.getResult()).thenReturn(documentSnapshot);
        when(task.isSuccessful()).thenReturn(true);
        when(documentSnapshot.getBoolean(KEY_ALLOW_NOTIFICATIONS)).thenReturn(true);
        assertTrue(firestoreRepository.getAreNotificationsAllowed(task));
    }
}
