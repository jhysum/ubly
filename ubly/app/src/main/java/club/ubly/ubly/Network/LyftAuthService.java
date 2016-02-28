package club.ubly.ubly.Network;

import club.ubly.ubly.Util.AccessTokenResponse;
import club.ubly.ubly.Util.UserTokenResponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jessesum on 2/27/16.
 */
public interface LyftAuthService {
    @POST("/oauth/token")
    @FormUrlEncoded
    Observable<AccessTokenResponse> getAccessToken(@Field("grant_type") String grantType,
                                                  @Field("scope") String scopeType);

    @POST("/oauth/token")
    @FormUrlEncoded
    Observable<UserTokenResponse> getUserToken(@Field("grant_type") String grantType,
                                               @Field("code") String code);
}
