package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.JSONBody.DispositionBody;
import call_recording.bkarogyam.com.management.Models.DispositionPOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by satyam on 15/6/18.
 */

public interface Disposition2Request {
    @POST("dispositions")
    Call<DispositionPOJO> call(@Body DispositionBody dispositionBody);
}
