package club.ubly.ubly.Network;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jessesum on 2/27/16.
 */
public interface UberApiService {

    @GET("/v1/products")
    Observable<Void> getProduct(@Query("latitude") String lat, @Query("longitude") String lon);
}
