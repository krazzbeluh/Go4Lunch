/*
 * FirebaseSignInWithCredentialClosure.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/27/20 5:13 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.closures;

import com.paulleclerc.go4lunch.enums.LoginState;

public interface FirebaseSignInWithCredentialClosure {
    void onComplete(LoginState loginState);
}
