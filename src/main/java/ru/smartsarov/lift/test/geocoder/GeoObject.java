
package ru.smartsarov.lift.test.geocoder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeoObject {

    @SerializedName("metaDataProperty")
    @Expose
    public MetaDataProperty_ metaDataProperty;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("boundedBy")
    @Expose
    public BoundedBy boundedBy;
    @SerializedName("Point")
    @Expose
    public Point point;

}
