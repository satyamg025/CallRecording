package call_recording.bkarogyam.com.management.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import call_recording.bkarogyam.com.management.Config.DbHandler;
import call_recording.bkarogyam.com.management.JSONBody.AssignCallBody;
import call_recording.bkarogyam.com.management.Models.AssignCallPOJO;
import call_recording.bkarogyam.com.management.Models.DataPOJO;
import call_recording.bkarogyam.com.management.Networking.ServiceGenerator;
import call_recording.bkarogyam.com.management.R;
import call_recording.bkarogyam.com.management.Requests.AssignCallRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignCallActivity extends AppCompatActivity {

    EditText name,title,details,mobile;
    TextView date;
    Button submit;
    String date_st="";
    Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM YYYY");
    SimpleDateFormat formatter2 = new SimpleDateFormat("YYYY-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_call);

        formatter=formatter2;
        name=(EditText)findViewById(R.id.name);
        date=(TextView)findViewById(R.id.call_date);
        title=(EditText)findViewById(R.id.title);
        details=(EditText)findViewById(R.id.details);
        mobile=(EditText)findViewById(R.id.mobile_no);

        submit=(Button)findViewById(R.id.submit);

        getSupportActionBar().setTitle("Assign Call");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Date today = new Date();
        date_st=formatter.format(today);
        date.setText(date_st);


        final DatePickerDialog.OnDateSetListener dates = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                view.setMaxDate(new Date().getTime());
                date.setText(formatter.format(myCalendar.getTime()));
                date_st=formatter.format(myCalendar.getTime());
            }

        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AssignCallActivity.this, dates, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=0;

                if(name.getText().toString().equals("")) {
                    flag = 1;
                    name.setError("Please enter valid name");
                }

                if(title.getText().toString().equals("")) {
                    flag = 1;
                    title.setError("Please enter valid title");
                }

                if(details.getText().toString().equals("")) {
                    flag=1;
                    details.setError("Please enter valid details");
                }

                if(mobile.getText().toString().equals("")) {
                    flag=1;
                    mobile.setError("Please enter valid mobile");
                }

                if(flag==0){
                    name.setError(null);
                    title.setError(null);
                    details.setError(null);
                    mobile.setError(null);

                    Date dat = null;
                    try {
                        dat = formatter.parse(date_st);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedDate = formatter2.format(dat)+"T18:30:00.000Z";

                    Log.e("date_fo",formattedDate);
                    final ProgressDialog progressDialog=new ProgressDialog(AssignCallActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    DataPOJO memberInfoPOJO=new Gson().fromJson(DbHandler.getString(AssignCallActivity.this,"member_info","{}"),DataPOJO.class);

                    AssignCallBody assignCallBody=new AssignCallBody(name.getText().toString(),date_st,memberInfoPOJO.getId(),title.getText().toString(),details.getText().toString(),mobile.getText().toString(),"Y");

                    Log.e("assign_body",new Gson().toJson(assignCallBody));
                    AssignCallRequest assignCallRequests= ServiceGenerator.createService(AssignCallRequest.class, DbHandler.getString(AssignCallActivity.this, "bearer", ""));
                    Call<AssignCallPOJO> call=assignCallRequests.call(assignCallBody);
                    call.enqueue(new Callback<AssignCallPOJO>() {
                        @Override
                        public void onResponse(Call<AssignCallPOJO> call, Response<AssignCallPOJO> response) {
                            progressDialog.dismiss();
                            if (response.code() == 200) {
                                Log.e("succed",new Gson().toJson(response.body()));
                                name.setText("");
                                title.setText("");
                                details.setText("");
                                mobile.setText("");
                                Toast.makeText(AssignCallActivity.this, "Call Successfully assigned", Toast.LENGTH_LONG).show();
                            } else if (response.code() == 403) {
                                progressDialog.dismiss();

                                Toast.makeText(AssignCallActivity.this, "Not Authorized", Toast.LENGTH_LONG).show();
                                DbHandler.unsetSession(AssignCallActivity.this, "isforcedLoggedOut");
                            } else {
                                progressDialog.dismiss();
                                new AlertDialog.Builder(AssignCallActivity.this).setTitle("Error").setMessage("Unable to connect to server")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).create().show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AssignCallPOJO> call, Throwable throwable) {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(AssignCallActivity.this).setTitle("Error").setMessage("Unable to connect to server")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).create().show();

                        }
                    });

                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AssignCallActivity.this,MainActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
