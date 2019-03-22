package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.JSONBody.IncomingBody;
import call_recording.bkarogyam.com.management.Models.IncomingPOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by satyam on 16/6/18.
 */

public interface IncomingRequest {
    @POST("new_call")
    Call<IncomingPOJO> call(@Body IncomingBody incomingBody);
}
