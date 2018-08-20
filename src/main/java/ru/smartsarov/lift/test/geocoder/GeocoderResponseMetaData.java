
package ru.smartsarov.lift.test.geocoder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeocoderResponseMetaData {

    @SerializedName("request")
    @Expose
    public String request;
    @SerializedName("found")
    @Expose
    public String found;
    @SerializedName("results")
    @Expose
    public String results;

}
