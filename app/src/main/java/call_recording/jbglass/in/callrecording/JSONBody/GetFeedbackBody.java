package call_recording.jbglass.in.callrecording.JSONBody;

import java.util.ArrayList;
import java.util.List;

public class GetFeedbackBody {
    List<String> disposition=new ArrayList<String>();

    public GetFeedbackBody(List<String> disposition){
        this.disposition=disposition;
    }
}
