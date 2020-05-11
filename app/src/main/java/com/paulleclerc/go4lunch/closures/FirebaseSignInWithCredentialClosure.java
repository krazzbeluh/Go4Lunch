package com.paulleclerc.go4lunch.closures;

import com.paulleclerc.go4lunch.enums.LoginState;

public interface FirebaseSignInWithCredentialClosure {
    void onComplete(LoginState loginState);
}
