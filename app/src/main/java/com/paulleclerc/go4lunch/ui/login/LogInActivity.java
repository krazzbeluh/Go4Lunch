/*
 * LogInActivity.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/29/20 3:34 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity implements LoginListener {
    private final int RC_SIGN_IN = 1;
    private static final String TAG =  LogInActivity.class.getSimpleName();

    private LoginViewModel viewModel;

    private GoogleSignInClient googleSignInClient;

    @BindView(R.id.sign_in_with_facebook_button)
    LoginButton signInWithFacebookButton;
    private CallbackManager facebookCallbackManager;

    @BindView(R.id.fullscreen_view)
    View fullscreenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.checkUserSignedIn();
        observeIsUserAuthenticated();

        configureSignInWithGoogle();
        configureSignInWithFacebook();
    }

    private void configureSignInWithGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @OnClick(R.id.sign_in_with_google_button)
    public void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void launchNextActivity() {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void showSnackBar(@StringRes int text) {
        Snackbar.make(fullscreenView, text, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) {
                    getGoogleAuthCredential(googleSignInAccount);
                }
            } catch (ApiException e) {
                Log.e(TAG, "onActivityResult: ", e);
            }
        }
    }

    private void getGoogleAuthCredential(GoogleSignInAccount googleSignInAccount) {
        String googleTokenId = googleSignInAccount.getIdToken();
        AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null);
        viewModel.signInWithCredential(googleAuthCredential);
    }

    private void configureSignInWithFacebook() {
        facebookCallbackManager = CallbackManager.Factory.create();
        signInWithFacebookButton.setReadPermissions("email", "public_profile");
        signInWithFacebookButton.registerCallback(facebookCallbackManager, viewModel.createFacebookLoginCallback());
    }

    private void observeIsUserAuthenticated() {
        viewModel.getIsUserAuthenticated().observe(this, isAuthenticated -> {
            switch (isAuthenticated) {
                case FAILED:
                    showSnackBar(R.string.authentication_failed);
                    break;
                case SIGNED_IN:
                    launchNextActivity();
                    break;
            }
        });
    }
}
