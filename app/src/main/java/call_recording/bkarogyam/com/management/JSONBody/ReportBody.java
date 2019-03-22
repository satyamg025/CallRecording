package call_recording.bkarogyam.com.management.JSONBody;

public class ReportBody {
    String complete;
    String emp_id,start,end;

    public ReportBody(String complete,String emp_id,String start,String end){
        this.complete=complete;
        this.emp_id=emp_id;
        this.start=start;
        this.end=end;
    }
}
