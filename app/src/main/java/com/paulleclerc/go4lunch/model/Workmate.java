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
