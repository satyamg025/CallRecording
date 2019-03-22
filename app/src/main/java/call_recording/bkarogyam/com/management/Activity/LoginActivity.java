package call_recording.bkarogyam.com.management.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import call_recording.bkarogyam.com.management.Config.DbHandler;
import call_recording.bkarogyam.com.management.JSONBody.LoginBody;
import call_recording.bkarogyam.com.management.Models.LoginPOJO;
import call_recording.bkarogyam.com.management.Networking.ServiceGenerator;
import call_recording.bkarogyam.com.management.R;
import call_recording.bkarogyam.com.management.Requests.LoginRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    TextInputLayout u_til, p_til;
    AppCompatButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

                    LoginBody loginBody=new LoginBody(username.getText().toString(),password.getText().toString());

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
                            if (response.code() == 200) {
                                if (!response.body().getSuccess()) {
                                    new AlertDialog.Builder(LoginActivity.this).setTitle("Login failed").setMessage("Invalid login credentials").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_LONG).show();
                                    Log.e("user_type",getIntent().getExtras().getString("user_type"));
                                    DbHandler.setSession(LoginActivity.this, response.body().getToken(),getIntent().getExtras().getString("user_type"));
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginPOJO> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }
}