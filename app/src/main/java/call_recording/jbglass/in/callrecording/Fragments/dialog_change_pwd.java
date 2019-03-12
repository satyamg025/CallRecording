package call_recording.jbglass.in.callrecording.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import call_recording.jbglass.in.callrecording.Activity.MainActivity;
import call_recording.jbglass.in.callrecording.Config.DbHandler;
import call_recording.jbglass.in.callrecording.JSONBody.ChangePasswordBody;
import call_recording.jbglass.in.callrecording.Models.DataPOJO;
import call_recording.jbglass.in.callrecording.Models.MemberInfoPOJO;
import call_recording.jbglass.in.callrecording.Models.RemarkPOJO;
import call_recording.jbglass.in.callrecording.Networking.ServiceGenerator;
import call_recording.jbglass.in.callrecording.R;
import call_recording.jbglass.in.callrecording.Requests.ChangePasswordRequest;
import call_recording.jbglass.in.callrecording.Requests.MemberInfoRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satyam on 30/7/18.
 */

public class dialog_change_pwd extends DialogFragment {

    EditText old_pwd,new_pwd,con_pwd;
    Button submit,cancel;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View parentView = inflater.inflate(R.layout.change_password, null);
        builder.setView(parentView);

        old_pwd=(EditText)parentView.findViewById(R.id.old_pwd);
        new_pwd=(EditText)parentView.findViewById(R.id.new_pwd);
        con_pwd=(EditText)parentView.findViewById(R.id.con_pwd);

        submit=(Button) parentView.findViewById(R.id.submit);
        cancel=(Button) parentView.findViewById(R.id.cancel);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(old_pwd.getText().toString().equals("") || new_pwd.getText().toString().equals("") || con_pwd.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please fill all fields",Toast.LENGTH_LONG).show();
                }
                else if(!new_pwd.getText().toString().equals(con_pwd.getText().toString())){
                    Toast.makeText(getActivity(),"Password doesn't  match",Toast.LENGTH_LONG).show();
                }
                else{
                    DataPOJO memberInfoPOJO=new Gson().fromJson(DbHandler.getString(getActivity(),"member_info","{}"),DataPOJO.class);
                    ChangePasswordBody changePasswordBody=new ChangePasswordBody(old_pwd.getText().toString(),new_pwd.getText().toString(),memberInfoPOJO.getEmpId().toString());
                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    ChangePasswordRequest memberInfoRequest = ServiceGenerator.createService(ChangePasswordRequest.class, DbHandler.getString(getActivity(), "bearer", ""));
                    Call<RemarkPOJO> memberInfoPOJOCall = memberInfoRequest.call(changePasswordBody);
                    memberInfoPOJOCall.enqueue(new Callback<RemarkPOJO>() {
                        @Override
                        public void onResponse(Call<RemarkPOJO> call, Response<RemarkPOJO> response) {
                            Log.e("code",String.valueOf(response.body().getMsg()));
                            Log.e("code1",String.valueOf(response.body().getSuccess()));
                            Log.e("code2",String.valueOf(response.code()));

                            if (response.code() == 200) {
                                progressDialog.dismiss();

                                if(!response.body().getSuccess()){
                                    Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();

                                    dismiss();
                                }
                                else{
                                    Toast.makeText(getActivity(),"Password changed successfully",Toast.LENGTH_LONG).show();
                                    dismiss();

                                }


                            } else if (response.code() == 403) {
                                progressDialog.dismiss();

                                Toast.makeText(getActivity(), "Not Authorized", Toast.LENGTH_LONG).show();
                                DbHandler.unsetSession(getActivity(), "isforcedLoggedOut");
                            } else {
                                progressDialog.dismiss();
                                new AlertDialog.Builder(getActivity()).setTitle("Error").setMessage("Unable to connect to server")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dismiss();
                                            }
                                        }).create().show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RemarkPOJO> call, Throwable t) {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(getActivity()).setTitle("Error").setMessage("Unable to connect to server")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //  dismiss();
                                        }
                                    }).create().show();

                        }
                    });
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        return builder.create();
    }
}
