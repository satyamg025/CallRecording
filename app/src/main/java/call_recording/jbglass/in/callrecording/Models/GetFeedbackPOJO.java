package call_recording.jbglass.in.callrecording.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetFeedbackPOJO {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<GetFeedbackDataPOJO> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<GetFeedbackDataPOJO> getData() {
        return data;
    }

    public void setData(List<GetFeedbackDataPOJO> data) {
        this.data = data;
    }
}
