package call_recording.bkarogyam.com.management.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 29/7/18.
 */

public class HistoryDatumPOJO {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("call_date")
    @Expose
    private String callDate;
    @SerializedName("emp_details")
    @Expose
    private List<EmpDetailPOJO> empDetails = null;
    @SerializedName("remarks")
    @Expose
    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public List<EmpDetailPOJO> getEmpDetails() {
        return empDetails;
    }

    public void setEmpDetails(List<EmpDetailPOJO> empDetails) {
        this.empDetails = empDetails;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
