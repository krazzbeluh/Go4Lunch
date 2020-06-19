/*
 * PlacesRepositoryTest.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/19/20 2:49 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.model.Workmate;
import com.paulleclerc.go4lunch.network.PlaceClient;
import com.paulleclerc.go4lunch.network.restaurant_response.Geometry;
import com.paulleclerc.go4lunch.network.restaurant_response.Location;
import com.paulleclerc.go4lunch.network.restaurant_response.OpeningHours;
import com.paulleclerc.go4lunch.network.restaurant_response.Photo;
import com.paulleclerc.go4lunch.network.restaurant_response.Result;
import com.paulleclerc.go4lunch.repository.PlacesRepository;
import com.paulleclerc.go4lunch.repository.WorkmatesRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.paulleclerc.go4lunch.repository.PlacesRepository.KEY_PLACE_ID;
import static com.paulleclerc.go4lunch.repository.PlacesRepository.KEY_USER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlacesRepositoryTest {
    PlacesRepository placesRepository;

    @Mock
    FirebaseFirestore db;
    @Mock
    FirebaseAuth auth;
    @Mock
    WorkmatesRepository workmatesRepository;
    @Mock
    PlaceClient placeClient;

    @Mock
    Result result;
    @Mock
    Geometry geometry;
    @Mock
    Location location;
    @Mock
    Photo photo;
    @Mock
    OpeningHours openingHours;
    @Mock
    Task<QuerySnapshot> task;
    @Mock
    QuerySnapshot querySnapshot;
    @Mock
    DocumentSnapshot documentSnapshot;

    Restaurant restaurantForTests = new Restaurant("go4lunch", "go4lunch", "go4lunch", "go4lunch", 0.0, new LatLng(0.0, 0.0), true, null);
    Workmate workmateForTests = new Workmate("uid", "username", "https://go4lunch.fr", "documentID", "chosenRestaurantID", "Go4Lunch");

    @Before
    public void setUp() {
        placesRepository = new PlacesRepository(db, auth, workmatesRepository, placeClient);
    }

    @Test
    public void testGetRestaurantsFromResultsShouldReturnNullIfResultsAreNull() {
        assertEquals(placesRepository.getRestaurantsFromResults(null), new ArrayList<>());
    }

    @Test
    public void testGetRestaurantsFromResultsShouldReturnNullIfResultsAreEmpty() {
        assertEquals(placesRepository.getRestaurantsFromResults(new ArrayList<>()), new ArrayList<>());
    }

    @Test
    public void testGetRestaurantsFromResultsShouldReturnAnObjectIfResultsAreNotEmpty() {
        when(result.getGeometry()).thenReturn(geometry);
        when(geometry.getLocation()).thenReturn(location);
        when(location.getLat()).thenReturn(0.0);
        when(location.getLng()).thenReturn(0.0);
        when(result.getPhotos()).thenReturn(Collections.singletonList(photo));
        when(photo.getPhotoReference()).thenReturn("photo");
        when(result.getOpeningHours()).thenReturn(openingHours);
        when(openingHours.getOpenNow()).thenReturn(true);
        when(result.getPlaceId()).thenReturn("azertyuiop");
        when(result.getName()).thenReturn("go4lunch");
        when(result.getVicinity()).thenReturn("Here");
        when(result.getRating()).thenReturn(3.0);
        assertEquals(placesRepository.getRestaurantsFromResults(Collections.singletonList(result)).size(), 1);
    }

    @Test
    public void testGetRestaurantsFromResultsShouldReturnNullIfResultsAreNotEmpty() {
        when(result.getGeometry()).thenReturn(geometry);
        when(geometry.getLocation()).thenReturn(location);
        when(location.getLat()).thenReturn(0.0);
        when(location.getLng()).thenReturn(0.0);
        when(result.getPhotos()).thenReturn(null);
        when(result.getOpeningHours()).thenReturn(openingHours);
        when(openingHours.getOpenNow()).thenReturn(true);
        when(result.getPlaceId()).thenReturn("azertyuiop");
        when(result.getName()).thenReturn("go4lunch");
        when(result.getVicinity()).thenReturn("Here");
        when(result.getRating()).thenReturn(3.0);
        assertEquals(placesRepository.getRestaurantsFromResults(Collections.singletonList(result)).size(), 1);
    }

    @Test
    public void testGetPlaceIdsFromRestaurantsShouldReturnEmptyArrayIfRestaurantsAreNull() {
        assertEquals(placesRepository.getRestaurantIdsFromRestaurants(null).size(), 0);
    }

    @Test
    public void testGetPlaceIdsFromRestaurantsShouldReturnEmptyArrayIfRestaurantsAreEmpty() {
        assertEquals(placesRepository.getRestaurantIdsFromRestaurants(new ArrayList<>()).size(), 0);
    }

    @Test
    public void testGetPlaceIdsFromRestaurantsShouldReturnNonEmptyArrayIfRestaurantsAreNot() {
        assertEquals(placesRepository.getRestaurantIdsFromRestaurants(Collections.singletonList(restaurantForTests)).size(),
                1);
    }

    @Test
    public void testIntegrateInterestedWorkmatesShouldReturnEmptyArrayListIfRestaurantsListIsNull() {
        assertEquals(placesRepository.integrateInterestedWorkmates(new ArrayList<>(), new HashMap<>()).size(), 0);
    }

    @Test
    public void testIntegrateInterestedWorkmatesShouldReturnEmptyArrayListIfRestaurantsListIsEmpty() {
        assertEquals(placesRepository.integrateInterestedWorkmates(Collections.singletonList(restaurantForTests), new HashMap<>()).size(), 1);
    }

    @Test
    public void testGetInterestedWorkmatesShouldReturnEmptyMapIfTaskFailure() {
        when(task.isSuccessful()).thenReturn(false);
        when(task.getResult()).thenReturn(querySnapshot);
        assertEquals(placesRepository.getInterestedWorkmates(task, null, null), new HashMap<>());
    }

    @Test
    public void testGetInterestedWorkmatesShouldReturnEmptyMapIfEmptyTask() {
        when(task.isSuccessful()).thenReturn(true);
        when(task.getResult()).thenReturn(null);
        assertEquals(placesRepository.getInterestedWorkmates(task, null, null), new HashMap<>());
    }

    @Test
    public void testGetInterestedWorkmatesShouldReturnEmptyMapIfEmptyPlaceIds() {
        when(task.isSuccessful()).thenReturn(true);
        when(task.getResult()).thenReturn(querySnapshot);
        assertEquals(placesRepository.getInterestedWorkmates(task, new ArrayList<>(), null), new HashMap<>());
    }

    @Test
    public void testGetInterestedWorkmatesShouldReturnEmptyMapIfEmptyDocuments() {
        String placeID = "placeID";

        when(task.isSuccessful()).thenReturn(true);
        when(task.getResult()).thenReturn(querySnapshot);
        when(querySnapshot.getDocuments()).thenReturn(new ArrayList<>());
        assertEquals(placesRepository.getInterestedWorkmates(task, Collections.singletonList(placeID), null), Collections.singletonMap(placeID, new ArrayList<>()));
    }

    @Test
    public void testGetInterestedWorkmatesShouldReturnEmptyMapIfPlaceIDIsNull() {
        String placeID = "placeID";

        when(task.isSuccessful()).thenReturn(true);
        when(task.getResult()).thenReturn(querySnapshot);
        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(documentSnapshot));
        when(documentSnapshot.getString(KEY_PLACE_ID)).thenReturn(null);
        assertEquals(placesRepository.getInterestedWorkmates(task, Collections.singletonList(placeID), null), Collections.singletonMap(placeID, new ArrayList<>()));
    }

    @Test
    public void testGetInterestedWorkmatesShouldReturnEmptyMapIfWorkmatesIsEmpty() {
        String placeID = "placeID";

        when(task.isSuccessful()).thenReturn(true);
        when(task.getResult()).thenReturn(querySnapshot);
        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(documentSnapshot));
        when(documentSnapshot.getString(KEY_PLACE_ID)).thenReturn(null);
        assertEquals(placesRepository.getInterestedWorkmates(task, Collections.singletonList(placeID), new ArrayList<>()), Collections.singletonMap(placeID, new ArrayList<>()));
    }

    @Test
    public void testGetInterestedWorkmatesShouldReturnNonEmptyMapIfWorkmatesIsNotEmpty() {
        String placeID = "placeID";

        when(task.isSuccessful()).thenReturn(true);
        when(task.getResult()).thenReturn(querySnapshot);
        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(documentSnapshot));
        when(documentSnapshot.getString(KEY_PLACE_ID)).thenReturn(placeID);
        when(documentSnapshot.getString(KEY_USER_ID)).thenReturn(workmateForTests.uid);
        assertEquals(placesRepository.getInterestedWorkmates(task, Collections.singletonList(placeID), Collections.singletonList(workmateForTests)).size(), 1);
    }

    @Test
    public void testGetWorkmatesIDsShouldReturnEmptyArrayListIfEmptyEntry() {
        assertEquals(placesRepository.getWorkmatesIds(new ArrayList<>()), new ArrayList<>());
    }

    @Test
    public void testGetWorkmatesIDsShouldReturnNotEmptyArrayListIfNotEmptyEntry() {
        assertEquals(placesRepository.getWorkmatesIds(Collections.singletonList(workmateForTests)).size(), 1);
    }

    @Test
    public void testGetInterestedWorkmatesFromTaskShouldReturnNullIfError() {
        when(task.getResult()).thenReturn(querySnapshot);
        when(task.isSuccessful()).thenReturn(false);
        assertNull(placesRepository.getInterestedWorkmatesFromTask(task, Collections.singletonList(workmateForTests)));
    }

    @Test
    public void testGetInterestedWorkmatesFromTaskShouldReturnNullIfResultsAreNull() {
        when(task.getResult()).thenReturn(null);
        when(task.isSuccessful()).thenReturn(true);
        assertNull(placesRepository.getInterestedWorkmatesFromTask(task, Collections.singletonList(workmateForTests)));
    }

    @Test
    public void testGetInterestedWorkmatesFromTaskShouldReturnEmptyArrayListIfEmptyWorkmates() {
        when(task.getResult()).thenReturn(querySnapshot);
        when(querySnapshot.getDocuments()).thenReturn(new ArrayList<>());
        when(task.isSuccessful()).thenReturn(true);
        assertEquals(placesRepository.getInterestedWorkmatesFromTask(task, new ArrayList<>()), new ArrayList<>());
    }

    @Test
    public void testGetInterestedWorkmatesFromTaskShouldReturnEmptyArrayListIfUninterestedWorkmate() {
        when(task.getResult()).thenReturn(querySnapshot);
        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(documentSnapshot));
        when(documentSnapshot.getString(KEY_USER_ID)).thenReturn("Invalid");
        when(task.isSuccessful()).thenReturn(true);
        assertEquals(placesRepository.getInterestedWorkmatesFromTask(task, Collections.singletonList(workmateForTests)), new ArrayList<>());
    }

    @Test
    public void testGetInterestedWorkmatesFromTaskShouldReturnNotEmptyArrayListIfInterestedWorkmate() {
        when(task.getResult()).thenReturn(querySnapshot);
        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(documentSnapshot));
        when(documentSnapshot.getString(KEY_USER_ID)).thenReturn(workmateForTests.uid);
        when(task.isSuccessful()).thenReturn(true);
        assertEquals(placesRepository.getInterestedWorkmatesFromTask(task, Collections.singletonList(workmateForTests)), Collections.singletonList(workmateForTests));
    }

    @Test
    public void testGetIsLikedFromTaskShouldReturnNullIfError() {
        when(task.isSuccessful()).thenReturn(false);
        when(task.getException()).thenReturn(new Exception("Erreur"));
        assertNull(placesRepository.getIsLikedFromTask(task));
    }

    @Test
    public void testGetIsLikedFromTaskShouldReturnNullIfNullResults() {
        when(task.isSuccessful()).thenReturn(true);
        when(task.getResult()).thenReturn(null);
        assertNull(placesRepository.getIsLikedFromTask(task));
    }

    @Test
    public void testGetIsLikedFromTaskShouldReturnFalseIfEmptyResults() {
        when(task.isSuccessful()).thenReturn(true);
        when(task.getResult()).thenReturn(querySnapshot);
        when(querySnapshot.getDocuments()).thenReturn(new ArrayList<>());
        assertFalse(placesRepository.getIsLikedFromTask(task));
    }

    @Test
    public void testGetIsLikedFromTaskShouldReturnTrueIfNotEmptyResults() {
        when(task.isSuccessful()).thenReturn(true);
        when(task.getResult()).thenReturn(querySnapshot);
        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(documentSnapshot));
        assertTrue(placesRepository.getIsLikedFromTask(task));
    }
}
