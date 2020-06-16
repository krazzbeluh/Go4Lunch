
/*
 * Period.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/15/20 6:10 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.network.restaurant_detail_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Period {

    @SerializedName("close")
    @Expose
    private Close close;
    @SerializedName("open")
    @Expose
    private Open open;

    public Close getClose() {
        return close;
    }

    public void setClose(Close close) {
        this.close = close;
    }

    public Open getOpen() {
        return open;
    }

    public void setOpen(Open open) {
        this.open = open;
    }

}
