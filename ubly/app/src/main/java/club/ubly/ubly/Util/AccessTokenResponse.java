package club.ubly.ubly.Util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jessesum on 2/27/16.
 */
public class AccessTokenResponse {

    @Expose
    @SerializedName("token_type")
    private String tokenType;

    @Expose
    @SerializedName("access_token")
    private String accessToken;

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
