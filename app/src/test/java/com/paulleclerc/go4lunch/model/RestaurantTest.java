/*
 * RestaurantTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/19/20 5:36 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;
import com.paulleclerc.go4lunch.network.restaurant_detail_response.Geometry;
import com.paulleclerc.go4lunch.network.restaurant_detail_response.Location;
import com.paulleclerc.go4lunch.network.restaurant_detail_response.Photo;
import com.paulleclerc.go4lunch.network.restaurant_detail_response.Result;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantTest {
    Restaurant restaurantForTests = new Restaurant("go4lunch", "go4lunch", "go4lunch", "go4lunch", 0.0, new LatLng(0.0, 0.0), true, null);

    @Test
    public void testConstructorWithResult() {
        String placeId = "placeId";
        String name = "name";
        String address = "address";

        Result result = mock(Result.class);
        Geometry geometry = mock(Geometry.class);
        Location location = mock(Location.class);
        Photo photo = mock(Photo.class);

        when(result.getName()).thenReturn(name);
        when(result.getVicinity()).thenReturn(address);
        when(result.getPhotos()).thenReturn(Collections.singletonList(photo));
        when(result.getGeometry()).thenReturn(geometry);
        when(geometry.getLocation()).thenReturn(location);
        when(location.getLat()).thenReturn(0.0);
        when(location.getLng()).thenReturn(0.0);
        when(result.getRating()).thenReturn(null);
        when(result.getOpeningHours()).thenReturn(null);
        when(result.getFormattedPhoneNumber()).thenReturn(null);
        when(result.getWebsite()).thenReturn(null);

        Restaurant restaurant = new Restaurant(placeId, result, null);

        assertEquals(restaurant.name, name);
        assertEquals(restaurant.id, placeId);
        assertEquals(restaurant.address, address);
        assertNull(restaurant.isOpened);
        assertEquals(restaurant.rate, Restaurant.Rate.UNKNOWN);

        when(result.getRating()).thenReturn(5.0);
        Restaurant restaurant2 = new Restaurant(placeId, result, null);
        assertEquals(restaurant2.rate, Restaurant.Rate.GOOD);
    }

    @Test
    public void testGetPhotoUrl() {
        assertNotNull(restaurantForTests.getPhotoUrl());
        Restaurant restaurant = new Restaurant("go4lunch", "go4lunch", "go4lunch", null, 0.0, new LatLng(0.0, 0.0), true, null);
        assertNull(restaurant.getPhotoUrl());
    }

    @Test
    public void testGetLocation() {
        assertEquals(restaurantForTests.getLocation(), new LatLng(0.0, 0.0));
    }

    @Test
    public void testGetDistance() {
        assertEquals(restaurantForTests.getDistance(new LatLng(1.0, 1.0)), (Integer) 157249);
    }
}
