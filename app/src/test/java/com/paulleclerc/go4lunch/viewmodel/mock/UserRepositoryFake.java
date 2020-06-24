/*
 * UserRepositoryFake.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 10:16 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.viewmodel.mock;

import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryFake extends UserRepository {
    List<Restaurant> restaurantsQueue = new ArrayList<>();
    List<String> stringsQueue = new ArrayList<>();

    public UserRepositoryFake() {
        super(null, null, null, null);
    }

    public void addRestaurantToQueue(Restaurant restaurant) {
        restaurantsQueue.add(restaurant);
    }

    public void addStringToQueue(String string) {
        stringsQueue.add(string);
    }

    @Override
    public void getChosenRestaurant(GetChosenRestaurantCompletion completion) {
        completion.onComplete(restaurantsQueue.remove(0));
    }

    @Override
    public void getUserAvatar(GetUserAvatarCompletion completion) {
        completion.onComplete(stringsQueue.remove(0));
    }
}
