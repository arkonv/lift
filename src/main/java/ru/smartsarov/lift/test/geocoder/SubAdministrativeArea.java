
package ru.smartsarov.lift.test.geocoder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubAdministrativeArea {

    @SerializedName("SubAdministrativeAreaName")
    @Expose
    public String subAdministrativeAreaName;
    @SerializedName("Locality")
    @Expose
    public Locality locality;

}
