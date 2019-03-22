package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.JSONBody.ReplicateCallBody;
import call_recording.bkarogyam.com.management.Models.RemarkPOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by satyam on 22/7/18.
 */

public interface ReplicateCall {
    @POST("recall")
    Call<RemarkPOJO> call(@Body ReplicateCallBody replicateCallBody);

}
