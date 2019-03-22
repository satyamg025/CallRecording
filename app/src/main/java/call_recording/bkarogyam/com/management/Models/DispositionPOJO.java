package call_recording.bkarogyam.com.management.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 15/6/18.
 */

public class DispositionPOJO {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("dispositions")
    @Expose
    private List<DispositionDatumPOJO> dispositions = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<DispositionDatumPOJO> getDispositions() {
        return dispositions;
    }

    public void setDispositions(List<DispositionDatumPOJO> dispositions) {
        this.dispositions = dispositions;
    }
}
