package call_recording.bkarogyam.com.management.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import call_recording.bkarogyam.com.management.Config.DbHandler;
import call_recording.bkarogyam.com.management.JSONBody.HistoryBody;
import call_recording.bkarogyam.com.management.Models.HistoryDataPOJO;
import call_recording.bkarogyam.com.management.Networking.ServiceGenerator;
import call_recording.bkarogyam.com.management.R;
import call_recording.bkarogyam.com.management.Requests.HistoryRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallHistoryFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TextView history;
    Button search;
    EditText mobile;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_call_history, container, false);

        mobile=(EditText)view.findViewById(R.id.mobile);
        search=(Button)view.findViewById(R.id.button);
        history=(TextView)view.findViewById(R.id.history);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mobile.getText().toString().equals("")){
                    mobile.setError("Please enter valid mobile no.");
                }
                else{
                    final ProgressDialog progressDialog=new ProgressDialog(getContext());
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    HistoryBody historyBody=new HistoryBody(mobile.getText().toString());

                    HistoryRequest historyRequest = ServiceGenerator.createService(HistoryRequest.class, DbHandler.getString(getContext(), "bearer", ""));
                    Call<HistoryDataPOJO> historyDataPOJOCall = historyRequest.call(historyBody);
                    historyDataPOJOCall.enqueue(new Callback<HistoryDataPOJO>() {
                        @Override
                        public void onResponse(Call<HistoryDataPOJO> call, Response<HistoryDataPOJO> response) {
                            Log.e("Res_code",String.valueOf(response.code()));
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

                                Toast.makeText(getContext(), "Not Authorized", Toast.LENGTH_LONG).show();
                                DbHandler.unsetSession(getContext(), "isforcedLoggedOut");
                            } else {
                                progressDialog.dismiss();
                                new android.support.v7.app.AlertDialog.Builder(getContext()).setTitle("Error").setMessage("Unable to connect to server")
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
                            Log.e("error_fail",t.getMessage());
                            new android.support.v7.app.AlertDialog.Builder(getContext()).setTitle("Error").setMessage("Unable to connect to server")
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
        return view;
    }   

}
