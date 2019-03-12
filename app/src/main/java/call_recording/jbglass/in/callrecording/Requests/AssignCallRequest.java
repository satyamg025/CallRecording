package call_recording.jbglass.in.callrecording.Requests;

import call_recording.jbglass.in.callrecording.JSONBody.AssignCallBody;
import call_recording.jbglass.in.callrecording.JSONBody.ChangePasswordBody;
import call_recording.jbglass.in.callrecording.Models.AssignCallPOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AssignCallRequest {
    @POST("new_call")
    Call<AssignCallPOJO> call(@Body AssignCallBody assignCallBody);
}
