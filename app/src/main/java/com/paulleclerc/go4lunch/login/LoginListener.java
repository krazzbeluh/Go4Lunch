package com.paulleclerc.go4lunch.login;

import androidx.annotation.StringRes;
import com.google.firebase.auth.AuthCredential;

public interface LoginListener {
    void launchNextActivity();
    void showSnackBar(@StringRes int text);
}