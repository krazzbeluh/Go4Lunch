
/*
 * PlusCode.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/9/20 2:11 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.network.restaurant_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlusCode {

    @SerializedName("compound_code")
    @Expose
    private String compoundCode;
    @SerializedName("global_code")
    @Expose
    private String globalCode;

    public String getCompoundCode() {
        return compoundCode;
    }

    public void setCompoundCode(String compoundCode) {
        this.compoundCode = compoundCode;
    }

    public String getGlobalCode() {
        return globalCode;
    }

    public void setGlobalCode(String globalCode) {
        this.globalCode = globalCode;
    }

}
