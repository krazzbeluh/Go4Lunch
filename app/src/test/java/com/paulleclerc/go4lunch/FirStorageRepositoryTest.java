/*
 * FirStorageRepositoryTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/18/20 4:15 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.paulleclerc.go4lunch.repository.FirStorageRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirStorageRepositoryTest {
    FirStorageRepository storageRepository;

    @Mock
    FirebaseStorage storage;

    @Mock
    Task<Uri> task;

    @Before
    public void setUp() {
        this.storageRepository = new FirStorageRepository(storage);
    }

    @Test
    public void testGetUriFromTaskShouldReturnNullIfError() {
        when(task.isSuccessful()).thenReturn(false);
        assertNull(storageRepository.getUriFromTask(task));
    }

    @Test
    public void testGetUriFromTaskShouldReturnNullIfNoUri() {
        when(task.isSuccessful()).thenReturn(true);
        when(task.getResult()).thenReturn(null);
        assertNull(storageRepository.getUriFromTask(task));
    }

    @Test
    public void testGetUriFromTaskShouldReturnUriIfValidTask() {
        Uri uri = Uri.EMPTY;

        when(task.isSuccessful()).thenReturn(true);
        when(task.getResult()).thenReturn(uri);
        assertEquals(storageRepository.getUriFromTask(task), uri);
    }
}
