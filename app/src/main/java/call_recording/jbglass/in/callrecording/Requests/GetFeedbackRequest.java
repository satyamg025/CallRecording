package call_recording.jbglass.in.callrecording.Requests;

import call_recording.jbglass.in.callrecording.JSONBody.GetFeedbackBody;
import call_recording.jbglass.in.callrecording.Models.GetFeedbackPOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetFeedbackRequest {
    @POST("display")
    Call<GetFeedbackPOJO> call(@Body GetFeedbackBody getFeedbackBody);
}
