package call_recording.bkarogyam.com.management.JSONBody;

import android.util.Log;

/**
 * Created by satyam on 15/6/18.
 */

public class DispositionBody {
    Object parent_id;
    String call_id;

    public DispositionBody(Object parent_id, String call_id){
        this.parent_id=parent_id;
        Log.e("p_id",String.valueOf(parent_id));
        this.call_id=call_id;
    }
}
