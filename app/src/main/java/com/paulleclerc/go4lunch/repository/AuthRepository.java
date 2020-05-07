package com.paulleclerc.go4lunch.repository;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;

public class AuthRepository {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public MutableLiveData<Boolean> firebaseSignInWithCredential(AuthCredential googleAuthCredential) {
        MutableLiveData<Boolean> isUserAuthenticated = new MutableLiveData<>();
        auth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> isUserAuthenticated.setValue(auth.getCurrentUser() != null));
        return isUserAuthenticated;
    }

    public MutableLiveData<Boolean> checkUserSignedIn() {
        MutableLiveData<Boolean> isUserAuthenticated = new MutableLiveData<>();
        isUserAuthenticated.setValue(auth.getCurrentUser() != null);
        return isUserAuthenticated;
    }
}
