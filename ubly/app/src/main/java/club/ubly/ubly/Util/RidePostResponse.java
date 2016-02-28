package club.ubly.ubly.Util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jessesum on 2/28/16.
 */
public class RidePostResponse {

    @Expose
    @SerializedName("ride_id")
    private int rideID;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("origin")
    private origin origin;

    @Expose
    @SerializedName("destination")
    private destination destination;

    @Expose
    @SerializedName("passenger")
    private passenger passenger;

    public int getRideID() {
        return rideID;
    }
}
