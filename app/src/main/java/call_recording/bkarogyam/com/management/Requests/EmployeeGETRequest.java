package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.Models.EmployeeResponsePOJO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface EmployeeGETRequest {
    @GET("employees")
    Call<EmployeeResponsePOJO> call();
}
