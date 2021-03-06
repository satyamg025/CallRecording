package call_recording.bkarogyam.com.management.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import call_recording.bkarogyam.com.management.Config.DbHandler;
import call_recording.bkarogyam.com.management.R;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;

public class StartActivity extends AppCompatActivity {

    Button employee,manager,admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_start);

        employee=(Button)findViewById(R.id.employee);
        manager=(Button)findViewById(R.id.manager);
        admin=(Button)findViewById(R.id.admin);

        Log.e("ccc","kkk");
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        if(DbHandler.getBoolean(this,"isLoggedIn",false)){
            if(DbHandler.getString(this,"user_type","").equals("employee")) {
                startActivity(new Intent(this, MainActivity.class).putExtra("action", "intent"));
                finish();
            }
            else if(DbHandler.getString(this,"user_type","").equals("manager")){
                startActivity(new Intent(this, ManagerActivity.class).putExtra("action", "intent"));
                finish();
            }
            else if(DbHandler.getString(this,"user_type","").equals("admin")){
                startActivity(new Intent(this, AdminActivity.class).putExtra("action", "intent"));
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

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StartActivity.this,LoginActivity.class);
                intent.putExtra("user_type","admin");
                startActivity(intent);
            }
        });
    }
}
