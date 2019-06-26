package call_recording.bkarogyam.com.management.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import call_recording.bkarogyam.com.management.Adapters.CallListAdapter;
import call_recording.bkarogyam.com.management.Config.DbHandler;
import call_recording.bkarogyam.com.management.Fragments.dialog_browse_directory;
import call_recording.bkarogyam.com.management.Fragments.dialog_change_pwd;
import call_recording.bkarogyam.com.management.JSONBody.CallingListBody;
import call_recording.bkarogyam.com.management.JSONBody.DispositionBody;
import call_recording.bkarogyam.com.management.JSONBody.IncomingBody;
import call_recording.bkarogyam.com.management.JSONBody.UploadBody;
import call_recording.bkarogyam.com.management.Models.CallingListPOJO;
import call_recording.bkarogyam.com.management.Models.DispositionPOJO;
import call_recording.bkarogyam.com.management.Models.IncomingPOJO;
import call_recording.bkarogyam.com.management.Models.RemarkPOJO;
import call_recording.bkarogyam.com.management.Networking.ServiceGenerator;
import call_recording.bkarogyam.com.management.R;
import call_recording.bkarogyam.com.management.Requests.CallingListRequest;
import call_recording.bkarogyam.com.management.Requests.Disposition2Request;
import call_recording.bkarogyam.com.management.Requests.IncomingRequest;
import call_recording.bkarogyam.com.management.Requests.UploadRequest;
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
        if(DbHandler.contains(MainActivity.this,"curr_chosen_directory"))
            uploadOutgoingFiles();

    }


    public void uploadOutgoingFiles(){

        File dir=null;
        String path = Environment.getExternalStorageDirectory().toString()+"/" + DbHandler.getString(MainActivity.this,"curr_chosen_directory","").replaceAll("%20"," ");
        dir = new File(path);

        File[] files = dir.listFiles();


        for (File file : files) {
            if (file.getName().startsWith("BKOut_")) {
                String call_id2=file.getName().split("_")[1];
                String callid=call_id2.split(".amr")[0];
                nullFeedback(callid);

                uploadFile(file.getAbsolutePath(),file.getName(),callid);

            }

        }
        if(progressDialog.isShowing())
            progressDialog.dismiss();

        uploadIncomingFiles();
    }

    void nullFeedback(String callid){
        final DispositionBody dispositionBody=new DispositionBody(null,callid);

//        progressDialog=new ProgressDialog(this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
        progressDialog.show();

        Disposition2Request dispositionsRequest= ServiceGenerator.createService(Disposition2Request.class, DbHandler.getString(this,"bearer",""));
        Call<DispositionPOJO> call=dispositionsRequest.call(dispositionBody);
        call.enqueue(new Callback<DispositionPOJO>() {
            @Override
            public void onResponse(Call<DispositionPOJO> call, Response<DispositionPOJO> response) {
                progressDialog.dismiss();
                if(response.code()==200){
                    Log.e("null","nullable");
                    return;
                }
                else if (response.code()==403){
                    Toast.makeText(MainActivity.this,"Not Authorized",Toast.LENGTH_LONG).show();
                    DbHandler.unsetSession(MainActivity.this,"isforcedLoggedOut");
                }
                else{
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
            public void onFailure(Call<DispositionPOJO> call, Throwable t) {
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

    public void uploadIncomingFiles(){
        File dir=null;
        String path = Environment.getExternalStorageDirectory().toString()+"/" + DbHandler.getString(MainActivity.this,"curr_chosen_directory","").replaceAll("%20"," ");
        dir = new File(path);

        File[] files = dir.listFiles();

        for (final File file : files) {
            if (file.getName().startsWith("BKInNum_")) {
                progressDialog.show();


                String cal_nu2 = file.getName().split("_")[1];
                final String num = cal_nu2.split(".amr")[0];

                IncomingBody incomingBody = new IncomingBody(num, "I");
                IncomingRequest incomingRequest = ServiceGenerator.createService(IncomingRequest.class, DbHandler.getString(MainActivity.this, "bearer", ""));
                Call<IncomingPOJO> call1 = incomingRequest.call(incomingBody);
                call1.enqueue(new Callback<IncomingPOJO>() {
                    @Override
                    public void onResponse(Call<IncomingPOJO> call, Response<IncomingPOJO> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            nullFeedback(response.body().getCall_id());
                            uploadFile(file.getAbsolutePath(), file.getName(), response.body().getCall_id());
                        } else if (response.code() == 403) {
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
                    public void onFailure(Call<IncomingPOJO> call, Throwable t) {
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
            else if(file.getName().startsWith("BKIn_")) {

                uploadFile(file.getAbsolutePath(), file.getName(), file.getName().split("_")[1]);

            }

        }
        if(progressDialog.isShowing())
            progressDialog.dismiss();

    }

    private void uploadFile(final String filePath, final String fileName,final String call_id) {
        class UF extends AsyncTask<String, String, String> {
            String responseString="";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                String fp = params[0];
                try {
                    HttpPost httpPost;

                    Log.e("fpath2",filePath);
                    Log.e("fname2",fileName);
                    Log.e("c_id2",call_id);

                    HttpClient httpClient = new DefaultHttpClient();
                    httpPost = new HttpPost("http://139.59.83.5:8081/api/uploads");
                    httpPost.addHeader("Authorization",DbHandler.getString(MainActivity.this,"bearer",""));

                    File file = new File(fp);

                    FileBody fileBody = new FileBody(file);
                    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                    multipartEntity.addPart("recording", fileBody);
                    httpPost.setEntity(multipartEntity);

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity entity = httpResponse.getEntity();
                    int statusCode = httpResponse.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(entity);
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }

                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                } catch (IOException e) {
                    responseString = e.toString();
                }

                return responseString;

            }

            @Override
            protected void onPostExecute(final String result) {

                Log.e("TAG", "Response from server: " + result);

                super.onPostExecute(result);
                String s = result.trim();
                Log.e("TAG", "Response from server: " + s);

                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String filename=jsonObject.getString("filename");
                    Log.e("fname",filename+" "+call_id);

                    //progressDialog.show();
                    UploadBody uploadBody=new UploadBody(filename,call_id);
                    Log.e("str_test",new Gson().toJson(uploadBody));

                    UploadRequest uploadRequest=ServiceGenerator.createService(UploadRequest.class,DbHandler.getString(MainActivity.this,"bearer",""));
                    Call<RemarkPOJO> call=uploadRequest.call(uploadBody);
                    call.enqueue(new Callback<RemarkPOJO>() {
                        @Override
                        public void onResponse(Call<RemarkPOJO> call, Response<RemarkPOJO> response) {
                            //progressDialog.dismiss();
                            File fil=new File(filePath);
                            boolean bool = fil.delete();

                            Log.e("error1234",String.valueOf(response.code()));
                            //Log.e("error123",response.body());
                        }

                        @Override
                        public void onFailure(Call<RemarkPOJO> call, Throwable t) {
                            // progressDialog.dismiss();
                            Log.e("update_Error",t.getMessage());
                        }
                    });
                    if(DbHandler.contains(MainActivity.this,"call_id"))
                        DbHandler.remove(MainActivity.this,"call_id");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }



        UF l = new UF();
        l.execute(filePath,fileName);


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
