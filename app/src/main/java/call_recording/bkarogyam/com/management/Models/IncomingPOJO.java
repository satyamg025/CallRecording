package call_recording.bkarogyam.com.management.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 16/6/18.
 */

public class IncomingPOJO {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("call_id")
    @Expose
    private String call_id;

    public Boolean getSuccess() {
        return success;
    }

    public String getCall_id() {
        return call_id;
    }

    public String getMsg() {
        return msg;
    }
}
