package club.ubly.ubly.Util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jessesum on 2/28/16.
 */
public class CancelPostResponse {
    @Expose
    @SerializedName("error")
    private String error;

    @Expose
    @SerializedName("error_detail")
    private ErrorDetail status;

    @Expose
    @SerializedName("amount")
    private int amount;

    @Expose
    @SerializedName("currency")
    private String currency;

    @Expose
    @SerializedName("token")
    private String token;

    @Expose
    @SerializedName("token_duration")
    private int tokenDuration;
}
