/*
 * Workmate.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/29/20 3:25 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.model;

import java.io.Serializable;

public class Workmate implements Serializable {
    public final String uid;
    public final String username;
    public final String avatarUri;
    private final String documentID;

    public Workmate(String uid, String username, String avatarUri, String documentID) {
        this.uid = uid;
        this.username = username;
        this.avatarUri = avatarUri;
        this.documentID = documentID;
    }

    public String getDocumentID() {
        return documentID;
    }
}

