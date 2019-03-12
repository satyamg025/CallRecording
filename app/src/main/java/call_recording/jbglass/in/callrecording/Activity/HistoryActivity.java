package call_recording.jbglass.in.callrecording.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import call_recording.jbglass.in.callrecording.Config.DbHandler;
import call_recording.jbglass.in.callrecording.JSONBody.HistoryBody;
import call_recording.jbglass.in.callrecording.Models.HistoryDataPOJO;
import call_recording.jbglass.in.callrecording.Networking.ServiceGenerator;
import call_recording.jbglass.in.callrecording.R;
import call_recording.jbglass.in.callrecording.Requests.HistoryRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    TextView history;
    Button search;
    EditText mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mobile=(EditText)findViewById(R.id.mobile);
        search=(Button)findViewById(R.id.button);
        history=(TextView)findViewById(R.id.history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Call History");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mobile.getText().toString().equals("")){
                    mobile.setError("Please enter valid mobile no.");
                }
                else{
                    final ProgressDialog progressDialog=new ProgressDialog(HistoryActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    HistoryBody historyBody=new HistoryBody(mobile.getText().toString());

                    HistoryRequest historyRequest = ServiceGenerator.createService(HistoryRequest.class, DbHandler.getString(HistoryActivity.this, "bearer", ""));
                    Call<HistoryDataPOJO> historyDataPOJOCall = historyRequest.call(historyBody);
                    historyDataPOJOCall.enqueue(new Callback<HistoryDataPOJO>() {
                        @Override
                        public void onResponse(Call<HistoryDataPOJO> call, Response<HistoryDataPOJO> response) {

                            if (response.code() == 200) {
                                progressDialog.dismiss();
                                StringBuilder msg= new StringBuilder("<ul style=\"list-style-type:disc\">");

                                if(response.body().getData().size()==0){
                                    msg.append("<li>&nbsp ").append("No History to show").append("</li>");
                                }
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    if(response.body().getData().get(i).getEmpDetails().size()>0){
                                        msg.append("<li>&nbsp ").append(response.body().getData().get(i).getEmpDetails().get(0).getName()).append(" (");
                                    }
                                    else{
                                        msg.append("<li>&nbsp --- (");
                                    }
                                    msg.append(response.body().getData().get(i).getCallDate().split("T")[0]).append(")<p>").append(response.body().getData().get(i).getRemarks()).append("</p></li>");
                                }


                                msg.append("</ul>");

                                history.setText(Html.fromHtml(msg.toString()));

                            } else if (response.code() == 403) {
                                progressDialog.dismiss();

                                Toast.makeText(HistoryActivity.this, "Not Authorized", Toast.LENGTH_LONG).show();
                                DbHandler.unsetSession(HistoryActivity.this, "isforcedLoggedOut");
                            } else {
                                progressDialog.dismiss();
                                new android.support.v7.app.AlertDialog.Builder(HistoryActivity.this).setTitle("Error").setMessage("Unable to connect to server")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).create().show();
                            }
                        }

                        @Override
                        public void onFailure(Call<HistoryDataPOJO> call, Throwable t) {
                            progressDialog.dismiss();
                            new android.support.v7.app.AlertDialog.Builder(HistoryActivity.this).setTitle("Error").setMessage("Unable to connect to server")
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
