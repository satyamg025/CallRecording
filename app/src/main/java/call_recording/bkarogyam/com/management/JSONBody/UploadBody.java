package call_recording.bkarogyam.com.management.JSONBody;

/**
 * Created by satyam on 15/6/18.
 */

public class UploadBody {
    String filename;
    String call_id;

    public UploadBody(String filename, String call_id){
        this.filename=filename;
        this.call_id=call_id;
    }
}
