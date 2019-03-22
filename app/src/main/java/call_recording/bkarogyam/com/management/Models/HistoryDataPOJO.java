package call_recording.bkarogyam.com.management.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 29/7/18.
 */

public class HistoryDataPOJO {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<HistoryDatumPOJO> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<HistoryDatumPOJO> getData() {
        return data;
    }

    public void setData(List<HistoryDatumPOJO> data) {
        this.data = data;
    }
}
