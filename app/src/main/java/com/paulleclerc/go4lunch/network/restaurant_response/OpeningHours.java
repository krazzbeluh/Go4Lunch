
/*
 * OpeningHours.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/9/20 2:11 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.network.restaurant_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

}
