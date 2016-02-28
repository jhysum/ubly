package club.ubly.ubly.Util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jessesum on 2/28/16.
 */
public class destination {

    @Expose
    @SerializedName("lat")
    private double lat;

    @Expose
    @SerializedName("lng")
    private double lng;

    @Expose
    @SerializedName("address")
    private String address;

    public destination(double latitude, double longtitude, String a) {
        lat = latitude;
        lng = longtitude;
        address = a;
    }
}
