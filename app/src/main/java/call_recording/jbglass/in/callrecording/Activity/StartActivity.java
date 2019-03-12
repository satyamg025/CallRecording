package call_recording.jbglass.in.callrecording.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import call_recording.jbglass.in.callrecording.Config.DbHandler;
import call_recording.jbglass.in.callrecording.R;

public class StartActivity extends AppCompatActivity {

    Button employee,manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        employee=(Button)findViewById(R.id.employee);
        manager=(Button)findViewById(R.id.manager);

        if(DbHandler.getBoolean(this,"isLoggedIn",false)){
            if(DbHandler.getString(this,"user_type","").equals("employee")) {
                startActivity(new Intent(this, MainActivity.class).putExtra("action", "intent"));
                finish();
            }
            else{
                startActivity(new Intent(this, ManagerActivity.class).putExtra("action", "intent"));
                finish();
            }
        }

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StartActivity.this,LoginActivity.class);
                intent.putExtra("user_type","employee");
                startActivity(intent);
            }
        });

        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StartActivity.this,LoginActivity.class);
                intent.putExtra("user_type","manager");
                startActivity(intent);
            }
        });
    }
}
