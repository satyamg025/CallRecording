package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.JSONBody.CallingListBody;
import call_recording.bkarogyam.com.management.Models.CallingListPOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by satyam on 15/6/18.
 */

public interface CallingListRequest {
    @POST("calling_list")
    Call<CallingListPOJO> call(@Body CallingListBody callingListBody);
}
