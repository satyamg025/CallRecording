package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.JSONBody.LoginBody;
import call_recording.bkarogyam.com.management.Models.LoginPOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by satyam on 21/2/18.
 */

public interface LoginRequest {
    @POST("authenticate")
    Call<LoginPOJO> call(@Body LoginBody loginBody);

}
