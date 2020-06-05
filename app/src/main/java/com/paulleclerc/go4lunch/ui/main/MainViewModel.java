/*
 * MainViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/5/20 9:45 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.paulleclerc.go4lunch.repository.AuthRepository;

public class MainViewModel extends AndroidViewModel {
    private final AuthRepository authService = new AuthRepository();

    private final MutableLiveData<Boolean> isUserSignedIn = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> isUserSignedIn() {
        return isUserSignedIn;
    }

    public void logOut() {
        isUserSignedIn.setValue(!authService.signOut());
    }
}
