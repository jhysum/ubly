package club.ubly.ubly.Util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jessesum on 2/28/16.
 */
public class ErrorDetail {
    @Expose
    @SerializedName("cancel_confirmation")
    private String cancelConfirmation;
}
