package com.paulleclerc.go4lunch.model;

import android.net.Uri;

public class Workmate {
    public final String uid;
    public final String username;
    public final Uri avatarUri;

    public Workmate(String uid, String username, Uri avatarUri) {
        this.uid = uid;
        this.username = username;
        this.avatarUri = avatarUri;
    }
}
