/*
 * PlacesRepository.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/18/20 12:31 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.repository;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
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

public class PlacesRepository {
    private static final Map<LatLng, List<Restaurant>> placesCache = new HashMap<>();
    private static final Map<String, String> namesCache = new HashMap<>();

    private static final String TAG = PlacesRepository.class.getSimpleName();
    private static final String KEY_USER_COLLECTION = "User";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_PLACE_ID = "chosenPlaceId";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final WorkmatesRepository workmatesRepository;
    private final PlaceClient client;

    public PlacesRepository(WorkmatesRepository workmatesRepository, PlaceClient client) {
        this.workmatesRepository = workmatesRepository;
        this.client = client;
    }

    public PlacesRepository() {
        this.workmatesRepository = new WorkmatesRepository();
        this.client = new PlaceClient();
    }

    public void fetchPlaces(LatLng userPosition, FetchPlacesCompletion completion) {
        List<Restaurant> restaurants = placesCache.get(userPosition);
        if (restaurants != null) {
            completion.onComplete(restaurants);
        } else {
            client.fetchRestaurants(userPosition, results -> {
                List<Restaurant> restaurantList = new ArrayList<>();
                List<String> restaurantIds = new ArrayList<>();
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
                    restaurantIds.add(result.getPlaceId());
                    restaurantList.add(restaurant);
                }

                fetchInterestedWorkmates(restaurantIds, interestedWorkmates -> {
                    for (Restaurant restaurant: restaurantList) {
                        restaurant.setInterestedWorkmates(interestedWorkmates.get(restaurant.id));
                    }

                    placesCache.put(userPosition, restaurantList);
                    completion.onComplete(restaurantList);
                });
            });
        }
    }

    private void fetchInterestedWorkmates(List<String> placeIDs, FetchInterestedWorkmatesForPlacesCompletion completion) {
        workmatesRepository.fetchWorkmates((success, workmates) -> {
            if (success) {
                List<String> workmatesIDs = new ArrayList<>();

                for (Workmate workmate : workmates) {
                    workmatesIDs.add(workmate.uid);
                }

                db.collection(KEY_USER_COLLECTION)
                        .whereIn(KEY_USER_ID, workmatesIDs)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                QuerySnapshot snapshot = task.getResult();
                                assert snapshot != null;
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

                                completion.onComplete(results);
                            } else {
                                Log.e(TAG, "fetchInterestedWorkmates: ", task.getException());
                                completion.onComplete(new HashMap<>());
                            }
                        });
            } else {
                completion.onComplete(new HashMap<>());
            }
        });
    }

    public void fetchInterestedWorkmates(String placeID, FetchInterestedWorkmatesForPlaceCompletion completion) {
        workmatesRepository.fetchWorkmates((success, workmates) -> {
            if (success) {
                List<String> workmatesIDs = new ArrayList<>();
                for (Workmate workmate : workmates) {
                    workmatesIDs.add(workmate.uid);
                }

                db.collection(KEY_USER_COLLECTION)
                        .whereIn(KEY_USER_ID, workmatesIDs)
                        .whereEqualTo(KEY_PLACE_ID, placeID)
                        .get()
                        .addOnCompleteListener(task -> {
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

                                completion.onComplete(workmatesList);
                            } else {
                                Log.e(TAG, "fetchInterestedWorkmates: ", task.getException());
                            }
                        });
            } else {
                completion.onComplete(null);
            }
        });
    }

    public void fetchDetail(String placeId, FetchDetailsCompletion completion) {
        if (placeId == null) completion.onComplete(null);
        client.fetchDetails(placeId, details -> this.fetchInterestedWorkmates(placeId, workmates -> {
            Restaurant restaurant = new Restaurant(placeId, details, workmates);
            namesCache.put(restaurant.id, restaurant.name);
            completion.onComplete(restaurant);
        }));
    }

    public void getName(String placeId, GetNameCompletion completion) {
        if (placeId == null) completion.onComplete(null);
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
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null) {
                        Log.e(TAG, "getIsLiked: ", task.getException());
                        completion.onComplete(false, null);
                        return;
                    }

                    completion.onComplete(true, task.getResult().getDocuments().size() > 0);
                });
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
                        completion.onComplete(false, null);
                    } else {
                        completion.onComplete(true, true);
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
                        completion.onComplete(true, null);
                        return;
                    }

                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    for (DocumentSnapshot document : documents) {
                        document.getReference().delete().addOnCompleteListener(task1 -> {
                            if (!task1.isSuccessful()) {
                                Log.e(TAG, "dislikePlace: ", task1.getException());
                                completion.onComplete(false, null);
                            } else {
                                completion.onComplete(true, false);
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
        void onComplete(boolean success, Boolean isLiked);
    }

    public interface GetNameCompletion {
        void onComplete(String name);
    }
}