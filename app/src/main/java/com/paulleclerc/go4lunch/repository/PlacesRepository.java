/*
 * PlacesRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/19/20 2:49 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paulleclerc.go4lunch.closures.FetchPlacesCompletion;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.model.Workmate;
import com.paulleclerc.go4lunch.network.PlaceClient;
import com.paulleclerc.go4lunch.network.restaurant_response.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

public class PlacesRepository {
    private static final Map<LatLng, List<Restaurant>> placesCache = new HashMap<>();
    private static final Map<String, String> namesCache = new HashMap<>();

    private static final String TAG = PlacesRepository.class.getSimpleName();
    private static final String KEY_USER_COLLECTION = "User";
    public static final String KEY_USER_ID = "userID";
    public static final String KEY_PLACE_ID = "chosenPlaceId";
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;
    private final WorkmatesRepository workmatesRepository;
    private final PlaceClient client;

    public PlacesRepository(FirebaseFirestore db, FirebaseAuth auth, WorkmatesRepository workmatesRepository, PlaceClient client) {
        this.workmatesRepository = workmatesRepository;
        this.client = client;
        this.db = db;
        this.auth = auth;
    }

    public PlacesRepository() {
        this.workmatesRepository = new WorkmatesRepository();
        this.client = new PlaceClient();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void fetchPlaces(LatLng userPosition, FetchPlacesCompletion completion) {
        List<Restaurant> restaurants = placesCache.get(userPosition);
        if (restaurants != null) {
            completion.onComplete(restaurants);
        } else {
            client.fetchRestaurants(userPosition, results -> {
                List<Restaurant> restaurantList = getRestaurantsFromResults(results);
                List<String> restaurantIds = getRestaurantIdsFromRestaurants(restaurantList);

                fetchInterestedWorkmates(restaurantIds, interestedWorkmates -> {
                    List<Restaurant> restaurantsList = integrateInterestedWorkmates(restaurantList, interestedWorkmates);

                    placesCache.put(userPosition, restaurantsList);
                    completion.onComplete(restaurantsList);
                });
            });
        }
    }

    public List<Restaurant> getRestaurantsFromResults(List<Result> results) {
        List<Restaurant> restaurantList = new ArrayList<>();
        if (results == null) return restaurantList;
        for (Result result : results) {
            LatLng restaurantLocation = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());

            String photoReference;
            if (result.getPhotos() == null) {
                photoReference = null;
            } else {
                photoReference = result.getPhotos().get(0).getPhotoReference();
            }

            Boolean isOpened;
            if (result.getOpeningHours() == null) isOpened = null;
            else isOpened = result.getOpeningHours().getOpenNow();

            Restaurant restaurant = new Restaurant(result.getPlaceId(), result.getName(), result.getVicinity(), photoReference, result.getRating(), restaurantLocation, isOpened, null);

            namesCache.put(restaurant.id, restaurant.name);
            restaurantList.add(restaurant);
        }

        return restaurantList;
    }

    public List<String> getRestaurantIdsFromRestaurants(List<Restaurant> restaurants) {
        List<String> restaurantIds = new ArrayList<>();
        if (restaurants == null) return restaurantIds;

        for (Restaurant restaurant : restaurants) {
            restaurantIds.add(restaurant.id);
        }

        return restaurantIds;
    }

    public List<Restaurant> integrateInterestedWorkmates(List<Restaurant> restaurants, Map<String, List<Workmate>> interestedWorkmates) {
        for (Restaurant restaurant : restaurants) {
            restaurant.setInterestedWorkmates(interestedWorkmates.get(restaurant.id));
        }
        return restaurants;
    }

    private void fetchInterestedWorkmates(List<String> placeIDs, FetchInterestedWorkmatesForPlacesCompletion completion) {
        workmatesRepository.fetchWorkmates((success, workmates) -> {
            if (success) {
                db.collection(KEY_USER_COLLECTION)
                        .whereIn(KEY_USER_ID, getWorkmatesIds(workmates))
                        .get()
                        .addOnCompleteListener(task -> completion.onComplete(getInterestedWorkmates(task, placeIDs, workmates)));
            } else {
                completion.onComplete(new HashMap<>());
            }
        });
    }

    public List<String> getWorkmatesIds(List<Workmate> workmates) {
        List<String> workmatesIDs = new ArrayList<>();

        for (Workmate workmate : workmates) {
            workmatesIDs.add(workmate.uid);
        }

        return workmatesIDs;
    }

    public Map<String, List<Workmate>> getInterestedWorkmates(Task<QuerySnapshot> task, List<String> placeIDs, List<Workmate> workmates) {
        QuerySnapshot snapshot = task.getResult();
        if (task.isSuccessful() && snapshot != null) {
            List<DocumentSnapshot> documentSnapshots = snapshot.getDocuments();

            // Map<PlaceID, List<WorkmateID>>
            Map<String, List<Workmate>> results = new HashMap<>();
            for (String placeID : placeIDs) {
                results.put(placeID, new ArrayList<>());
            }

            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                String placeID = documentSnapshot.getString(KEY_PLACE_ID);
                String userID = documentSnapshot.getString(KEY_USER_ID);

                if (placeID != null && userID != null && placeIDs.contains(placeID)) {
                    Workmate workmate = null;
                    for (Workmate mate : workmates) {
                        if (mate.uid.equals(userID)) {
                            workmate = mate;
                            break;
                        }
                    }

                    if (workmate != null)
                        Objects.requireNonNull(results.get(placeID))
                                .add(workmate);
                }
            }

            return results;
        } else {
            Log.e(TAG, "fetchInterestedWorkmates: ", task.getException());
            return new HashMap<>();
        }
    }

    void fetchInterestedWorkmates(String placeID, FetchInterestedWorkmatesForPlaceCompletion completion) {
        workmatesRepository.fetchWorkmates((success, workmates) -> {
            if (success) {
                db.collection(KEY_USER_COLLECTION)
                        .whereIn(KEY_USER_ID, getWorkmatesIds(workmates))
                        .whereEqualTo(KEY_PLACE_ID, placeID)
                        .get()
                        .addOnCompleteListener(task -> completion.onComplete(getInterestedWorkmatesFromTask(task, workmates)));
            } else {
                completion.onComplete(null);
            }
        });
    }

    public List<Workmate> getInterestedWorkmatesFromTask(Task<QuerySnapshot> task, @Nonnull List<Workmate> workmates) {
        QuerySnapshot results = task.getResult();
        if (task.isSuccessful() && results != null) {
            List<Workmate> workmatesList = new ArrayList<>();
            for (DocumentSnapshot document : results.getDocuments()) {
                for (Workmate workmate : workmates) {
                    if (workmate.uid.equals(document.getString(KEY_USER_ID))) {
                        workmatesList.add(workmate);
                        break;
                    }
                }
            }

            return workmatesList;
        } else {
            Log.e(TAG, "fetchInterestedWorkmates: ", task.getException());
            return null;
        }
    }

    public void fetchDetail(@Nonnull String placeId, FetchDetailsCompletion completion) {
        client.fetchDetails(placeId, details -> this.fetchInterestedWorkmates(placeId, workmates -> {
            Restaurant restaurant = new Restaurant(placeId, details, workmates);
            namesCache.put(restaurant.id, restaurant.name);
            completion.onComplete(restaurant);
        }));
    }

    public void getName(@Nonnull String placeId, GetNameCompletion completion) {
        String name = namesCache.get(placeId);
        if (name == null) {
            client.fetchDetails(placeId, details -> {
                String placeName = details.getName();
                namesCache.put(placeId, placeName);
                completion.onComplete(placeName);
            });
        } else {
            completion.onComplete(name);
        }
    }

    public void getIsLiked(String id, LikeRestaurantCompletion completion) {
        db.collection("Like")
                .whereEqualTo("placeID", id)
                .whereEqualTo("userID", Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .get()
                .addOnCompleteListener(task -> completion.onComplete(getIsLikedFromTask(task)));
    }

    public Boolean getIsLikedFromTask(Task<QuerySnapshot> task) {
        if (!task.isSuccessful() || task.getResult() == null) {
            Log.e(TAG, "getIsLiked: ", task.getException());
            return null;
        }

        return task.getResult().getDocuments().size() > 0;
    }

    public void likePlace(String id, LikeRestaurantCompletion completion) {
        Map<String, Object> documentData = new HashMap<>();
        documentData.put("placeID", id);
        documentData.put("userID", Objects.requireNonNull(auth.getCurrentUser()).getUid());

        db.collection("Like")
                .document()
                .set(documentData)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "likePlace: ", task.getException());
                        completion.onComplete(null);
                    } else {
                        completion.onComplete(true);
                    }
                });
    }

    public void dislikePlace(String id, LikeRestaurantCompletion completion) {
        db.collection("Like")
                .whereEqualTo("placeID", id)
                .whereEqualTo("userID", Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null) {
                        Log.e(TAG, "dislikePlace: ", task.getException());
                        completion.onComplete(null);
                        return;
                    }

                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    for (DocumentSnapshot document : documents) {
                        document.getReference().delete().addOnCompleteListener(task1 -> {
                            if (!task1.isSuccessful()) {
                                Log.e(TAG, "dislikePlace: ", task1.getException());
                                completion.onComplete(null);
                            } else {
                                completion.onComplete(false);
                            }
                        });
                    }
                });
    }

    private interface FetchInterestedWorkmatesForPlacesCompletion {
        void onComplete(Map<String, List<Workmate>> results);
    }

    public interface FetchInterestedWorkmatesForPlaceCompletion {
        void onComplete(List<Workmate> workmates);
    }

    public interface FetchDetailsCompletion {
        void onComplete(Restaurant restaurant);
    }

    public interface LikeRestaurantCompletion {
        void onComplete(Boolean isLiked);
    }

    public interface GetNameCompletion {
        void onComplete(String name);
    }
}