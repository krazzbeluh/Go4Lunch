/*
 * Workmate.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/18/20 12:47 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.model;

import java.io.Serializable;

public class Workmate implements Serializable {
    public final String uid;
    public final String username;
    public final String avatarUri;
    private final String documentID;
    private final String chosenRestaurantId;
    private final String chosenRestaurantName;

    public Workmate(String uid, String username, String avatarUri, String documentID, String chosenRestaurantId, String chosenRestaurantName) {
        this.uid = uid;
        this.username = username;
        this.avatarUri = avatarUri;
        this.documentID = documentID;
        this.chosenRestaurantId = chosenRestaurantId;
        this.chosenRestaurantName = chosenRestaurantName;
    }

    public String getDocumentID() {
        return documentID;
    }

    public String getChosenRestaurantId() {
        return chosenRestaurantId;
    }

    public String getChosenRestaurantName() {
        return chosenRestaurantName;
    }
}

