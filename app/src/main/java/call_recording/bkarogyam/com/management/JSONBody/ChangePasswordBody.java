package call_recording.bkarogyam.com.management.JSONBody;

/**
 * Created by satyam on 30/7/18.
 */

public class ChangePasswordBody {
    String user_password,password,emp_id;

    public ChangePasswordBody(String user_password,String password,String emp_id){
        this.user_password=user_password;
        this.password=password;
        this.emp_id=emp_id;
    }
}
