package com.paulleclerc.go4lunch.ui.login;

import androidx.annotation.StringRes;

public interface LoginListener {
    void launchNextActivity();
    void showSnackBar(@StringRes int text);
}