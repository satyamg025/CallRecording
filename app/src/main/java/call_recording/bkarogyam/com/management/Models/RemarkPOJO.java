package call_recording.bkarogyam.com.management.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 15/6/18.
 */

public class RemarkPOJO {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }
}
