/*
 * LoginViewModelTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 2:48 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.paulleclerc.go4lunch.ui.login.LoginViewModel;
import com.paulleclerc.go4lunch.viewmodel.mock.AuthRepositoryFake;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.paulleclerc.go4lunch.enums.LoginState.FAILED;
import static com.paulleclerc.go4lunch.enums.LoginState.NONE;
import static com.paulleclerc.go4lunch.enums.LoginState.SIGNED_IN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class LoginViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    LoginViewModel viewModel;

    @Mock
    Application application;
    AuthRepositoryFake authRepository;

    @Before
    public void setUp() {
        authRepository = new AuthRepositoryFake();
        viewModel = new LoginViewModel(application, authRepository);
    }

    @Test
    public void testCheckUserSignedInShouldStoreCorrectState() {
        authRepository.addBooleanToQueue(false);
        authRepository.addBooleanToQueue(true);

        assertNull(viewModel.getIsUserAuthenticated().getValue());
        viewModel.checkUserSignedIn();
        assertEquals(NONE, viewModel.getIsUserAuthenticated().getValue());

        viewModel.checkUserSignedIn();
        assertEquals(SIGNED_IN, viewModel.getIsUserAuthenticated().getValue());
    }

    @Test
    public void testSignInWithCredentialShouldReturnStateSentToCallback() {
        authRepository.addLoginStateToQueue(NONE);
        authRepository.addLoginStateToQueue(FAILED);
        authRepository.addLoginStateToQueue(SIGNED_IN);

        assertNull(viewModel.getIsUserAuthenticated().getValue());

        viewModel.signInWithCredential(null);
        assertEquals(NONE, viewModel.getIsUserAuthenticated().getValue());

        viewModel.signInWithCredential(null);
        assertEquals(FAILED, viewModel.getIsUserAuthenticated().getValue());

        viewModel.signInWithCredential(null);
        assertEquals(SIGNED_IN, viewModel.getIsUserAuthenticated().getValue());
    }
}
