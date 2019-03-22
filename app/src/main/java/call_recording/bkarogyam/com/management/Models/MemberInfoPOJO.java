package call_recording.bkarogyam.com.management.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 15/6/18.
 */

public class MemberInfoPOJO {


    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private DataPOJO data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public DataPOJO getData() {
        return data;
    }

    public void setData(DataPOJO data) {
        this.data = data;
    }

}
