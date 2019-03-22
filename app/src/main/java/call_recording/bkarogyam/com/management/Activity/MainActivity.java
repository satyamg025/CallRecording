package call_recording.bkarogyam.com.management.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import call_recording.bkarogyam.com.management.Adapters.CallListAdapter;
import call_recording.bkarogyam.com.management.Config.DbHandler;
import call_recording.bkarogyam.com.management.Fragments.dialog_browse_directory;
import call_recording.bkarogyam.com.management.Fragments.dialog_change_pwd;
import call_recording.bkarogyam.com.management.JSONBody.CallingListBody;
import call_recording.bkarogyam.com.management.Models.CallingListPOJO;
import call_recording.bkarogyam.com.management.Networking.ServiceGenerator;
import call_recording.bkarogyam.com.management.R;
import call_recording.bkarogyam.com.management.Requests.CallingListRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    static private int CALL_PERMISSION_CODE = 23;
    RecyclerView recyclerView;
    CallListAdapter callListAdapter;
    ProgressDialog progressDialog;
    TextView date_tv,no_data;
    String date_st="";
    LinearLayout date_layout;
    Calendar myCalendar = Calendar.getInstance();

    private List<PhoneAccountHandle> phoneAccountHandleList;
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM YYYY");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Date today = new Date();
        date_st=formatter.format(today);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                view.setMaxDate(new Date().getTime());
                date_tv.setText(formatter.format(myCalendar.getTime()));
                date_st=formatter.format(myCalendar.getTime());
                callingList();
            }

        };


        date_layout=(LinearLayout)findViewById(R.id.date_layout);
        date_tv=(TextView)findViewById(R.id.date);
        no_data=(TextView)findViewById(R.id.no_data);
        date_tv.setText(date_st);
        date_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        if(DbHandler.getString(MainActivity.this,"curr_chosen_directory","").equals("")){
            dialog_browse_directory dialog= new dialog_browse_directory();
            dialog.show(getSupportFragmentManager(),"");
        }

        DbHandler.putBoolean(MainActivity.this,"not_first",true);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        callingList();

    }


    void callingList(){
        progressDialog.show();
        CallingListBody callingListBody=new CallingListBody(date_st);

        CallingListRequest callingListRequest = ServiceGenerator.createService(CallingListRequest.class, DbHandler.getString(MainActivity.this, "bearer", ""));
        Call<CallingListPOJO> callingListPOJO = callingListRequest.call(callingListBody);
        callingListPOJO.enqueue(new Callback<CallingListPOJO>() {
            @Override
            public void onResponse(Call<CallingListPOJO> call, Response<CallingListPOJO> response) {

                progressDialog.dismiss();
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    DbHandler.putString(MainActivity.this, "calling_list", gson.toJson(response.body().getData()));

                    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    if(response.body().getData().size()>0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);

                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        callListAdapter = new CallListAdapter(MainActivity.this, getSupportFragmentManager(), response.body().getData());
                        recyclerView.setAdapter(callListAdapter);
                    }
                    else{
                        recyclerView.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);
                    }

                    int PERMISSION_ALL = 1;
                    String[] PERMISSIONS = {Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.MODIFY_PHONE_STATE, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.ACCESS_NOTIFICATION_POLICY, Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE, Manifest.permission.CAPTURE_AUDIO_OUTPUT};

                    if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
                    }
                } else if (response.code() == 403) {
                    Log.e("not_auth2","auth_not2");

                    Toast.makeText(MainActivity.this, "Not Authorized", Toast.LENGTH_LONG).show();
                    DbHandler.unsetSession(MainActivity.this, "isforcedLoggedOut");
                } else {
                    new AlertDialog.Builder(MainActivity.this).setTitle("Error").setMessage("Unable to connect to server")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onBackPressed();
                                }
                            }).create().show();
                }
            }

            @Override
            public void onFailure(Call<CallingListPOJO> call, Throwable t) {
                progressDialog.dismiss();
                new AlertDialog.Builder(MainActivity.this).setTitle("Error").setMessage("Unable to connect to server")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onBackPressed();
                            }
                        }).create().show();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void call() {

        DbHandler.putString(MainActivity.this,"call_id","");
        TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+getIntent().getExtras().getString("number")));

        startActivity(intent);
        finish();

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CALL_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }else{
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.logout){
            DbHandler.unsetSession(this,"logout");
        }
        else if(id==R.id.assign_call){
            startActivity(new Intent(MainActivity.this,AssignCallActivity.class));
        }
        else if(id==R.id.call_history){
            startActivity(new Intent(MainActivity.this,HistoryActivity.class));
        }
        else if(id==R.id.change_pwd){
            dialog_change_pwd dialog= new dialog_change_pwd();
            dialog.show(getSupportFragmentManager(),"");
        }
        else if(id==R.id.call_rec_path){
            dialog_browse_directory dialog= new dialog_browse_directory();
            dialog.show(getSupportFragmentManager(),"");
        }

        return super.onOptionsItemSelected(item);
    }
}
