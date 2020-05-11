package com.paulleclerc.go4lunch.repository;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.paulleclerc.go4lunch.closures.FirebaseSignInWithCredentialClosure;
import com.paulleclerc.go4lunch.enums.LoginState;

public class AuthRepository {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public void firebaseSignInWithCredential(AuthCredential googleAuthCredential, FirebaseSignInWithCredentialClosure completion) {
        auth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> completion.onComplete(authTask.isSuccessful() && checkUserSignedIn() ? LoginState.SIGNED_IN : LoginState.FAILED ));
    }

    public boolean checkUserSignedIn() {
        return auth.getCurrentUser() != null;
    }
}
