
/*
 * Northeast.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/9/20 2:11 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.network.restaurant_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Northeast {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

}
