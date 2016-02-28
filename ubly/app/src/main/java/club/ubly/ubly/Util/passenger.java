package club.ubly.ubly.Util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jessesum on 2/28/16.
 */
public class passenger {

    @Expose
    @SerializedName("first_name")
    private String firstName;

    @Expose
    @SerializedName("phone_number")
    private String phoneNumber;
}
