
package ru.smartsarov.lift.test.geocoder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DependentLocality {

    @SerializedName("DependentLocalityName")
    @Expose
    public String dependentLocalityName;
    @SerializedName("Thoroughfare")
    @Expose
    public Thoroughfare thoroughfare;

}
