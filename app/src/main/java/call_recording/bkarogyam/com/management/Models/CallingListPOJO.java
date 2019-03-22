package call_recording.bkarogyam.com.management.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 15/6/18.
 */

public class CallingListPOJO {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<ListDatumPOJO> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ListDatumPOJO> getData() {
        return data;
    }

    public void setData(List<ListDatumPOJO> data) {
        this.data = data;
    }
}
