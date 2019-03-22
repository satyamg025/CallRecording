package call_recording.bkarogyam.com.management.JSONBody;

/**
 * Created by satyam on 22/7/18.
 */

public class ReplicateCallBody {
    String call_id;
    String call_date;

    public ReplicateCallBody(String call_id,String call_date){
        this.call_id=call_id;
        this.call_date=call_date;
    }
}
