package call_recording.jbglass.in.callrecording.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeeResponsePOJO {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("employees")
    @Expose
    private List<EmployeeDataPOJO> employees = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<EmployeeDataPOJO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDataPOJO> employees) {
        this.employees = employees;
    }
}
