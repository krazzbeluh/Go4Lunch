/*
 * AuthRepositoryFake.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 2:48 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel.mock;

import com.google.firebase.auth.AuthCredential;
import com.paulleclerc.go4lunch.enums.LoginState;
import com.paulleclerc.go4lunch.repository.AuthRepository;

import java.util.ArrayList;
import java.util.List;

public class AuthRepositoryFake extends AuthRepository {
    List<Boolean> booleansQueue = new ArrayList<>();
    List<LoginState> loginStatesQueue = new ArrayList<>();
    AuthCredential credential;

    public AuthRepositoryFake() {
        super(null);
    }

    public void addBooleanToQueue(boolean bool) {
        booleansQueue.add(bool);
    }

    public void addLoginStateToQueue(LoginState state) {
        loginStatesQueue.add(state);
    }

    @Override
    public void firebaseSignInWithCredential(AuthCredential googleAuthCredential,
                                             FirebaseSignInWithCredentialCompletion completion) {
        completion.onComplete(loginStatesQueue.remove(0));
    }

    @Override
    public boolean checkUserSignedIn() {
        return booleansQueue.remove(0);
    }
}
