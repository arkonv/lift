
package ru.smartsarov.lift.test.geocoder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thoroughfare {

    @SerializedName("ThoroughfareName")
    @Expose
    public String thoroughfareName;
    @SerializedName("Premise")
    @Expose
    public Premise premise;

}
