package call_recording.jbglass.in.callrecording.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import call_recording.jbglass.in.callrecording.Config.DbHandler;
import call_recording.jbglass.in.callrecording.JSONBody.LoginBody;
import call_recording.jbglass.in.callrecording.Models.LoginPOJO;
import call_recording.jbglass.in.callrecording.Networking.ServiceGenerator;
import call_recording.jbglass.in.callrecording.R;
import call_recording.jbglass.in.callrecording.Requests.LoginRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    TextInputLayout u_til, p_til;
    AppCompatButton submit, register;
    Gson gson = new Gson();
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
//        notificationIntent.addCategory("android.intent.category.DEFAULT");
//
//        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.SECOND, 15);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

        getSupportActionBar().setTitle("Login");

        if(DbHandler.getBoolean(this,"isLoggedIn",false)){
            startActivity(new Intent(this,MainActivity.class).putExtra("action","intent"));
            finish();
        }

        username = (EditText) findViewById(R.id.input_roll);
        password = (EditText) findViewById(R.id.input_password);
        u_til = (TextInputLayout) findViewById(R.id.input_roll_layout);
        p_til = (TextInputLayout) findViewById(R.id.input_password_layout);
        submit = (AppCompatButton) findViewById(R.id.btn_login);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("")) {
                    u_til.setError("Please enter valid email-id");
                }

                if (password.getText().toString().equals("")) {
                    p_til.setError("Please enter password");
                }

                if (!username.getText().toString().equals("") && !password.getText().toString().equals("")) {

                    Log.e("username",username.getText().toString()+" "+password.getText().toString());
                    LoginBody loginBody=new LoginBody(username.getText().toString(),password.getText().toString());

                    //Gson gson=new Gson();
                    Log.e("str_test",gson.toJson(loginBody));
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    LoginRequest loginRequest = ServiceGenerator.createService(LoginRequest.class);
                    Call<LoginPOJO> call = loginRequest.call(loginBody);
                    call.enqueue(new Callback<LoginPOJO>() {
                        @Override
                        public void onResponse(Call<LoginPOJO> call, Response<LoginPOJO> response) {
                            progressDialog.dismiss();
                            Log.e("error", String.valueOf(response.code()));
                            Log.e("data", String.valueOf(gson.toJson(response.body())));
                            if (response.code() == 200) {
                                if (!response.body().getSuccess()) {
                                    new AlertDialog.Builder(LoginActivity.this).setTitle("Login failed").setMessage("Invalid login credentials").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_LONG).show();
                                    DbHandler.setSession(LoginActivity.this, response.body().getToken());
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginPOJO> call, Throwable t) {
                            Log.e("error123",t.toString());
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }
}