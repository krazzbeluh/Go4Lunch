/*
 * AuthRepositoryTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/9/20 3:25 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paulleclerc.go4lunch.repository.AuthRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthRepositoryTest {
    AuthRepository authRepository;

    @Mock
    FirebaseAuth auth;
    @Mock
    FirebaseUser user;
    @Mock
    AuthCredential credential;
    @Mock
    Task<AuthResult> task;

    @Before
    public void setUp() {
        authRepository = new AuthRepository(auth);
    }

    @Test
    public void testSignOutShouldReturnTrueIfUserSignedOut() {
        when(auth.getCurrentUser()).thenReturn(null);

        assertTrue(authRepository.signOut());
    }

    @Test
    public void testSignOutShouldReturnFalseIfError() {
        when(auth.getCurrentUser()).thenReturn(user);

        assertFalse(authRepository.signOut());
    }

    @Test
    public void testGetUidShouldReturnNullIfUserNotSignedIn() {
        when(auth.getCurrentUser()).thenReturn(null);

        assertNull(authRepository.getUid());
    }

    @Test
    public void testGetUidShouldReturnStringIfUserNotSignedIn() {
        String result = "FakeUser1";
        when(user.getUid()).thenReturn(result);
        when(auth.getCurrentUser()).thenReturn(user);

        assertEquals(authRepository.getUid(), result);
    }

    @Test
    public void testFirebaseSignInWithCredentialShouldCallCompletionLambda() {
        when(auth.signInWithCredential(credential)).thenReturn(task);
    }
}
