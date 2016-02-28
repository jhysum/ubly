package club.ubly.ubly.Util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jessesum on 2/28/16.
 */
public class DestinationRequest {

    @Expose
    @SerializedName("lat")
    private double lat;

    @Expose
    @SerializedName("lng")
    private double lng;

    @Expose
    @SerializedName("lng")
    private String address;

    public DestinationRequest(double latitude, double longtitude, String a) {
        lat = latitude;
        lng = longtitude;
        address = a;
    }
}
