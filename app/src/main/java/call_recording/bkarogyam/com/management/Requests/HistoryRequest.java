package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.JSONBody.HistoryBody;
import call_recording.bkarogyam.com.management.Models.HistoryDataPOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by satyam on 29/7/18.
 */

public interface HistoryRequest {
    @POST("history")
    Call<HistoryDataPOJO> call(@Body HistoryBody historyBody);
}
