/*
 * AuthRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 9:42 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paulleclerc.go4lunch.enums.LoginState;

public class AuthRepository {
    private final FirebaseAuth auth;

    public AuthRepository() {
        auth = FirebaseAuth.getInstance();
    }

    public AuthRepository(FirebaseAuth auth) {
        this.auth = auth;
    }

    public void firebaseSignInWithCredential(AuthCredential googleAuthCredential,
                                             FirebaseSignInWithCredentialCompletion completion) {
        auth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> completion.onComplete(getState(authTask)));
    }

    public LoginState getState(Task<AuthResult> authTask) {
        return authTask.isSuccessful() && checkUserSignedIn() ? LoginState.SIGNED_IN : LoginState.FAILED;
    }

    public boolean signOut() {
        auth.signOut();
        return !checkUserSignedIn();
    }

    public boolean checkUserSignedIn() {
        return auth.getCurrentUser() != null;
    }

    public String getUid() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return null;
        return user.getUid();
    }

    public String getEmail() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) return user.getEmail();
        else return null;
    }

    public interface FirebaseSignInWithCredentialCompletion {
        void onComplete(LoginState loginState);
    }
}
