package call_recording.bkarogyam.com.management.JSONBody;

/**
 * Created by satyam on 15/6/18.
 */

public class RemarkBody {
    String remark;
    String call_id;

    public RemarkBody(String remark, String call_id){
        this.remark=remark;
        this.call_id=call_id;
    }
}
