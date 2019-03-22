package call_recording.bkarogyam.com.management.Requests;

import call_recording.bkarogyam.com.management.JSONBody.ReportBody;
import call_recording.bkarogyam.com.management.Models.ReportResponsePOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReportRequest {
    @POST("report")
    Call<ReportResponsePOJO> call(@Body ReportBody reportBody);
}
