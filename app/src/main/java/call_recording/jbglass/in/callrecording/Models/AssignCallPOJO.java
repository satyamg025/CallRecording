package call_recording.jbglass.in.callrecording.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignCallPOJO {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("call_id")
    @Expose
    private String callId;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }
}
