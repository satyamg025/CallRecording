package call_recording.jbglass.in.callrecording.Requests;

import call_recording.jbglass.in.callrecording.Models.EmployeeResponsePOJO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface EmployeeRequest {
    @GET("employees")
    Call<EmployeeResponsePOJO> call();
}
