/*
 * AuthRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/5/20 9:49 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paulleclerc.go4lunch.closures.FirebaseSignInWithCredentialClosure;
import com.paulleclerc.go4lunch.enums.LoginState;

public class AuthRepository {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public void firebaseSignInWithCredential(AuthCredential googleAuthCredential, FirebaseSignInWithCredentialClosure completion) {
        auth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> completion.onComplete(authTask.isSuccessful() && checkUserSignedIn() ? LoginState.SIGNED_IN : LoginState.FAILED));
    }

    public boolean signOut() {
        auth.signOut();
        return (auth.getCurrentUser() == null);
    }

    public boolean checkUserSignedIn() {
        return auth.getCurrentUser() != null;
    }

    String getUid() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return null;
        return user.getUid();
    }
}
