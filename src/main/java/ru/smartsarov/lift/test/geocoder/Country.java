
package ru.smartsarov.lift.test.geocoder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("AddressLine")
    @Expose
    public String addressLine;
    @SerializedName("CountryNameCode")
    @Expose
    public String countryNameCode;
    @SerializedName("CountryName")
    @Expose
    public String countryName;
    @SerializedName("AdministrativeArea")
    @Expose
    public AdministrativeArea administrativeArea;

}
