
package ru.smartsarov.lift.test.geocoder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeocoderMetaData {

    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("precision")
    @Expose
    public String precision;
    @SerializedName("Address")
    @Expose
    public Address address;
    @SerializedName("AddressDetails")
    @Expose
    public AddressDetails addressDetails;

}
