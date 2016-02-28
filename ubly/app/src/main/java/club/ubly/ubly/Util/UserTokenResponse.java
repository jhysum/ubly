package club.ubly.ubly.Util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jessesum on 2/28/16.
 */
public class UserTokenResponse {

    @Expose
    @SerializedName("token_type")
    private String tokenType;

    @Expose
    @SerializedName("access_token")
    private String accessToken;

    @Expose
    @SerializedName("refresh_token")
    private String refreshToken;

    @Expose
    @SerializedName("expires_in")
    private int expireIn;

    @Expose
    @SerializedName("scope")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }
}
