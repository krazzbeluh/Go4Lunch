/*
 * LoginViewModel.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/27/20 5:13 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.login;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.paulleclerc.go4lunch.enums.LoginState;
import com.paulleclerc.go4lunch.repository.AuthRepository;

public class LoginViewModel extends AndroidViewModel {
    private static final String TAG = LoginViewModel.class.getSimpleName();

    private final AuthRepository authRepository;
    MutableLiveData<LoginState> isUserAuthenticated = new MutableLiveData<>();

    public LoginViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    void checkUserSignedIn() {
        isUserAuthenticated.setValue(authRepository.checkUserSignedIn() ? LoginState.SIGNED_IN : LoginState.NONE);
    }

    void signInWithCredential(AuthCredential credential) {
        authRepository.firebaseSignInWithCredential(credential, loginState -> isUserAuthenticated.setValue(loginState));
    }

    FacebookCallback<LoginResult> createFacebookLoginCallback() {
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                signInWithCredential(credential);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {
                isUserAuthenticated.setValue(LoginState.FAILED);
            }
        };
    }
}
