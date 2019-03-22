package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.Models.MemberInfoPOJO;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by satyam on 15/6/18.
 */

public interface MemberInfoRequest {
    @GET("memberinfo")
    Call<MemberInfoPOJO> call();
}
