package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.JSONBody.ChangePasswordBody;
import call_recording.bkarogyam.com.management.Models.RemarkPOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by satyam on 30/7/18.
 */

public interface ChangePasswordRequest {
    @POST("update_password")
    Call<RemarkPOJO> call(@Body ChangePasswordBody changePasswordBody);
}
