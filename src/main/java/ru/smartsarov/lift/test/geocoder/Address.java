
package ru.smartsarov.lift.test.geocoder;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("country_code")
    @Expose
    public String countryCode;
    @SerializedName("formatted")
    @Expose
    public String formatted;
    @SerializedName("Components")
    @Expose
    public List<Component> components = null;

}
