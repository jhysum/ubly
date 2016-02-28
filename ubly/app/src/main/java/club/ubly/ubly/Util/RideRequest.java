package club.ubly.ubly.Util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jessesum on 2/28/16.
 */
public class RideRequest {

    @Expose
    @SerializedName("ride_type")
    private String rideType;

    @Expose
    @SerializedName("origin")
    private origin origin;

    @Expose
    @SerializedName("destination")
    private destination destination;

    public RideRequest(String ridetype, origin Origin, destination Destination) {
        rideType = ridetype;
        origin = Origin;
        destination = Destination;
    }
}
