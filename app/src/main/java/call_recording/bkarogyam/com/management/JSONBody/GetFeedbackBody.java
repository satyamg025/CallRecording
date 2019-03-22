package call_recording.bkarogyam.com.management.JSONBody;

import java.util.ArrayList;
import java.util.List;

public class GetFeedbackBody {
    List<String> disposition=new ArrayList<String>();

    public GetFeedbackBody(List<String> disposition){
        this.disposition=disposition;
    }
}
