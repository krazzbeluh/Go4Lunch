/*
 * Workmate.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/27/20 5:13 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.model;

import java.io.Serializable;

public class Workmate implements Serializable {
    public final String uid;
    public final String username;
    public final String avatarUri;

    public Workmate(String uid, String username, String avatarUri) {
        this.uid = uid;
        this.username = username;
        this.avatarUri = avatarUri;
    }
}
