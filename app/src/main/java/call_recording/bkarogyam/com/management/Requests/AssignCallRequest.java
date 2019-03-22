package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.JSONBody.AssignCallBody;
import call_recording.bkarogyam.com.management.Models.AssignCallPOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AssignCallRequest {
    @POST("new_call")
    Call<AssignCallPOJO> call(@Body AssignCallBody assignCallBody);
}
