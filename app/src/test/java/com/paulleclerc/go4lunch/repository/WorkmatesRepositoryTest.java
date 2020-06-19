/*
 * WorkmatesRepositoryTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/19/20 4:34 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.paulleclerc.go4lunch.repository.WorkmatesRepository.WORKMATE_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkmatesRepositoryTest {
    WorkmatesRepository workmatesRepository;

    @Mock
    FirebaseFirestore db;
    @Mock
    AuthRepository auth;
    @Mock
    FirStorageRepository storage;

    @Mock
    Task<QuerySnapshot> task;
    @Mock
    QuerySnapshot querySnapshot;
    @Mock
    DocumentSnapshot documentSnapshot;

    @Before
    public void setUp() {
        workmatesRepository = new WorkmatesRepository(db, auth, storage);
    }

    @Test
    public void testGetDocumentsFromTaskShouldReturnNullIfError() {
        when(task.getResult()).thenReturn(querySnapshot);
        when(task.isSuccessful()).thenReturn(false);
        when(task.getException()).thenReturn(new Exception("Erreur"));
        assertNull(workmatesRepository.getDocumentsFromTask(task));
    }

    @Test
    public void testGetDocumentsFromTaskShouldReturnNullIfResultNull() {
        when(task.getResult()).thenReturn(null);
        when(task.isSuccessful()).thenReturn(true);
        assertNull(workmatesRepository.getDocumentsFromTask(task));
    }

    @Test
    public void testGetDocumentsFromTaskShouldReturnDocumentsIfResultContainsDocuments() {
        List<DocumentSnapshot> response = Collections.singletonList(documentSnapshot);

        when(task.getResult()).thenReturn(querySnapshot);
        when(task.isSuccessful()).thenReturn(true);
        when(querySnapshot.getDocuments()).thenReturn(response);
        assertEquals(workmatesRepository.getDocumentsFromTask(task), response);
    }

    @Test
    public void testGetDocumentsIdsShouldReturnEmptyMapIfEmptyDocumentsList() {
        assertEquals(workmatesRepository.getWorkmatesDocumentsIds(new ArrayList<>()), new HashMap<>());
    }

    @Test
    public void testGetDocumentsIdsShouldReturnEmptyMapIfAssociatedUsersIsNull() {
        when(documentSnapshot.get(WORKMATE_KEY)).thenReturn(null);
        assertEquals(workmatesRepository.getWorkmatesDocumentsIds(Collections.singletonList(documentSnapshot)), new HashMap<>());
    }

    @Test
    public void testGetDocumentsIdsShouldNotContainUserIdIfAssociatedUsersContainsUserIdAsFirstEntry() {
        String firstId = "firstId";
        String secondId = "secondId";
        when(documentSnapshot.get(WORKMATE_KEY)).thenReturn(Arrays.asList(firstId, secondId));
        when(auth.getUid()).thenReturn(firstId);
        assertNull(workmatesRepository.getWorkmatesDocumentsIds(Collections.singletonList(documentSnapshot)).get(firstId));
    }

    @Test
    public void testGetDocumentsIdsShouldNotContainUserIdIfAssociatedUsersContainsUserIdAsSecondEntry() {
        String firstId = "firstId";
        String secondId = "secondId";
        when(documentSnapshot.get(WORKMATE_KEY)).thenReturn(Arrays.asList(firstId, secondId));
        when(auth.getUid()).thenReturn(secondId);
        assertNull(workmatesRepository.getWorkmatesDocumentsIds(Collections.singletonList(documentSnapshot)).get(firstId));
    }
}
