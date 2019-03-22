package call_recording.bkarogyam.com.management.JSONBody;

/**
 * Created by satyam on 15/6/18.
 */

public class DispositionBody {
    String parent_id;
    String call_id;

    public DispositionBody(String parent_id, String call_id){
        this.parent_id=parent_id;
        this.call_id=call_id;
    }
}
