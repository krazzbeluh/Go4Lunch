package com.paulleclerc.go4lunch.login;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.*;
import com.paulleclerc.go4lunch.repository.AuthRepository;

public class LoginViewModel extends AndroidViewModel {
    private static final String TAG = LoginViewModel.class.getSimpleName();

    private AuthRepository authRepository;
    LiveData<Boolean> isUserAuthenticated;

    public LoginViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    public void checkUserSignedIn() {
        this.isUserAuthenticated = authRepository.checkUserSignedIn();
    }

    void signInWithCredential(AuthCredential credential) {
        isUserAuthenticated = authRepository.firebaseSignInWithCredential(credential);
    }

    FacebookCallback<LoginResult> createFacebookLoginCallback() {
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                //activity.showSnackBar(R.string.authentication_failed);
            }
        };
    }
}
