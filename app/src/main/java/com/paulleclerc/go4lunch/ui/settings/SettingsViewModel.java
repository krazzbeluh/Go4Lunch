/*
 * SettingsViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/5/20 11:58 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.settings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SettingsViewModel extends AndroidViewModel {
    private MutableLiveData<String> avatarUrl = new MutableLiveData<>();

    public SettingsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getUserAvatarUrl() {
        return avatarUrl;
    }
}
