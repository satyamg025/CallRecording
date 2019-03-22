package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.Models.DispositionPOJO;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by satyam on 15/6/18.
 */

public interface DispositionsRequest {
    @GET("dispositions")
    Call<DispositionPOJO> call();
}
