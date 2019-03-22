package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.JSONBody.GetFeedbackBody;
import call_recording.bkarogyam.com.management.Models.GetFeedbackPOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetFeedbackRequest {
    @POST("display")
    Call<GetFeedbackPOJO> call(@Body GetFeedbackBody getFeedbackBody);
}
