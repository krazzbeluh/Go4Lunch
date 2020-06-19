/*
 * MainViewModel.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/19/20 3:36 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.repository.AuthRepository;
import com.paulleclerc.go4lunch.repository.FirestoreRepository;
import com.paulleclerc.go4lunch.repository.UserRepository;

public class MainViewModel extends AndroidViewModel {
    private final AuthRepository authService = new AuthRepository();
    private final UserRepository userRepository = new UserRepository();
    private final FirestoreRepository firestore = new FirestoreRepository();

    private final MutableLiveData<Boolean> isUserSignedIn = new MutableLiveData<>();
    private final MutableLiveData<String> userAvatarUrl = new MutableLiveData<>();
    private final MutableLiveData<String> userEmail = new MutableLiveData<>();
    private final MutableLiveData<String> username = new MutableLiveData<>();
    private final MutableLiveData<Restaurant> chosenRestaurant = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        userRepository.setFCMToken();
    }

    public LiveData<Boolean> isUserSignedIn() {
        return isUserSignedIn;
    }

    public LiveData<String> getUserAvatar() {
        userRepository.getUserAvatar(userAvatarUrl::setValue);
        return userAvatarUrl;
    }

    public LiveData<String> getUserEmail() {
        String email = authService.getEmail();
        userEmail.setValue(email);
        return userEmail;
    }

    public LiveData<String> getUsername() {
        firestore.getUsername(username::setValue);
        return username;
    }

    public LiveData<Restaurant> getChosenRestaurant() {
        userRepository.getChosenRestaurant(chosenRestaurant::setValue);
        return chosenRestaurant;
    }

    public void logOut() {
        isUserSignedIn.setValue(!authService.signOut());
    }
}
