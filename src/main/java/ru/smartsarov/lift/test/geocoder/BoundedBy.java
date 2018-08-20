
package ru.smartsarov.lift.test.geocoder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BoundedBy {

    @SerializedName("Envelope")
    @Expose
    public Envelope envelope;

}
