package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.JSONBody.UploadBody;
import call_recording.bkarogyam.com.management.Models.RemarkPOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by satyam on 15/6/18.
 */

public interface UploadRequest {
    @POST("update_file")
    Call<RemarkPOJO> call(@Body UploadBody uploadBody);
}
