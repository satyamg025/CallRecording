package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.JSONBody.EmployeeBody;
import call_recording.bkarogyam.com.management.Models.EmployeeResponsePOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmployeeRequest {
    @POST("managers_employees")
    Call<EmployeeResponsePOJO> call(@Body EmployeeBody employeeBody);
}
