package call_recording.jbglass.in.callrecording.Requests;

import call_recording.jbglass.in.callrecording.JSONBody.EmployeeBody;
import call_recording.jbglass.in.callrecording.Models.EmployeeResponsePOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EmployeeGETRequest {
    @GET("employees")
    Call<EmployeeResponsePOJO> call();
}
