/*
 * LoginListener.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/27/20 5:13 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.login;

import androidx.annotation.StringRes;

public interface LoginListener {
    void launchNextActivity();

    void showSnackBar(@StringRes int text);
}