package com.paulleclerc.go4lunch.repository;

import android.content.Context;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paulleclerc.go4lunch.closures.FetchPlacesCompletion;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.model.Workmate;
import com.paulleclerc.go4lunch.model.restaurant_response.Result;
import com.paulleclerc.go4lunch.network.PlaceClient;

import java.util.*;

public class PlacesRepository {
    private static final Map<LatLng, List<Restaurant>> placesCache = new HashMap<>();

    private static final String TAG = PlacesRepository.class.getSimpleName();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final WorkmatesRepository workmatesRepository;
    private final Context context;
    private final PlaceClient client;

    public PlacesRepository(WorkmatesRepository workmatesRepository, Context context, PlaceClient client) {
        this.workmatesRepository = workmatesRepository;
        this.context = context;
        this.client = client;
    }

    public PlacesRepository(Context context) {
        this.context = context;
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

                    Double rating = result.getRating();
                    Restaurant.Rate rate;
                    if (rating == null) {
                        rate = Restaurant.Rate.UNKNOWN;
                    } else {
                        rating = result.getRating() / 5 * 3;

                        if (rating < 1) {
                            rate = Restaurant.Rate.BAD;
                        } else if (rating < 2) {
                            rate = Restaurant.Rate.MEDIUM;
                        } else {
                            rate = Restaurant.Rate.GOOD;
                        }
                    }

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

                    restaurantIds.add(result.getPlaceId());
                    restaurantList.add(new Restaurant(result.getPlaceId(), result.getName(), result.getVicinity(), photoReference, rate, restaurantLocation, getDistance(userPosition, restaurantLocation), isOpened, null));
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

    private void fetchInterestedWorkmates(List<String> placeIDs, FetchInterestedWorkmatesCompletion completion) {
        workmatesRepository.fetchWorkmates(context, (success, workmates) -> {
            if (success) {
                List<String> workmatesIDs = new ArrayList<>();

                for (Workmate workmate : workmates) {
                    workmatesIDs.add(workmate.uid);
                }

                db.collection("Interests")
                        .whereIn("userID", workmatesIDs)
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
                                    String placeID = documentSnapshot.getString("placeID");
                                    String userID = documentSnapshot.getString("userID");

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

    public void fetchDetail(Restaurant restaurant, FetchDetailsCompletion completion) {
        client.fetchDetails(restaurant, details -> completion.onComplete(new Restaurant.RestaurantDetails(details.getFormattedPhoneNumber(), details.getWebsite())));
    }

    private Integer getDistance(@Nullable LatLng StartP, LatLng EndP) {
        if (StartP == null) return null;
        int Radius = 6371;// radius of earth in Km (Type1, Type2) -> TypeR in {}
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return (int) (Radius * c * 1000);
    }

    private interface FetchInterestedWorkmatesCompletion {
        void onComplete(Map<String, List<Workmate>> results);
    }

    public interface FetchDetailsCompletion {
        void onComplete(Restaurant.RestaurantDetails details);
    }
}