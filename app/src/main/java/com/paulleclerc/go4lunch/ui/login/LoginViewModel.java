/*
 * LoginViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 2:48 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.login;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.paulleclerc.go4lunch.enums.LoginState;
import com.paulleclerc.go4lunch.repository.AuthRepository;

public class LoginViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<LoginState> isUserAuthenticated = new MutableLiveData<>();

    public LoginViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    public LoginViewModel(Application application, AuthRepository authRepository) {
        super(application);
        this.authRepository = authRepository;
    }

    public LiveData<LoginState> getIsUserAuthenticated() {
        return isUserAuthenticated;
    }

    public void checkUserSignedIn() {
        isUserAuthenticated.setValue(authRepository.checkUserSignedIn() ? LoginState.SIGNED_IN :
                LoginState.NONE);
    }

    public void signInWithCredential(AuthCredential credential) {
        authRepository.firebaseSignInWithCredential(credential, isUserAuthenticated::setValue);
    }

    public FacebookCallback<LoginResult> createFacebookLoginCallback() {
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                signInWithCredential(FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken()));
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                isUserAuthenticated.setValue(LoginState.FAILED);
            }
        };
    }
}
