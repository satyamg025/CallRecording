package call_recording.jbglass.in.callrecording.Requests;

import call_recording.jbglass.in.callrecording.JSONBody.DispositionBody;
import call_recording.jbglass.in.callrecording.JSONBody.ReportBody;
import call_recording.jbglass.in.callrecording.Models.DispositionPOJO;
import call_recording.jbglass.in.callrecording.Models.ReportResponsePOJO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReportRequest {
    @POST("report")
    Call<ReportResponsePOJO> call(@Body ReportBody reportBody);
}
