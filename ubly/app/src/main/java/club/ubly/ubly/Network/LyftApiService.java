package club.ubly.ubly.Network;

import club.ubly.ubly.Util.CancelPostResponse;
import club.ubly.ubly.Util.DestinationRequest;
import club.ubly.ubly.Util.OriginRequest;
import club.ubly.ubly.Util.RidePostResponse;

import club.ubly.ubly.Util.RideRequest;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by jessesum on 2/27/16.
 */
public interface LyftApiService {

    @POST("/rides/")
    Observable<RidePostResponse> postRide(@Body RideRequest request);

    @POST("/rides/{ride_id}/cancel")
    Observable<CancelPostResponse> cancelRide(@Path("ride_id") int rideId);
}
