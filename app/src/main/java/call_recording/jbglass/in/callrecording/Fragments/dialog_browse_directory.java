package call_recording.jbglass.in.callrecording.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class dialog_browse_directory extends DialogFragment {

    TextView path;
    Button browse;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View parentView = inflater.inflate(R.layout.dialog_browse_folder, null);
        builder.setView(parentView);

        browse=(Button)parentView.findViewById(R.id.browse);
        path=(TextView)parentView.findViewById(R.id.curr_dir);

        if(!DbHandler.getString(getActivity(),"curr_chosen_directory","").equals("")){
            path.setText("/"+DbHandler.getString(getActivity(),"curr_chosen_directory","").replace("%20"," "));
        }
        else{
            path.setText("No folder selected");
        }

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                intent.putExtra("CONTENT_TYPE", "*/*");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(intent, 1);

            }
        });
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String Fpath = data.getDataString();
        //TODO handle your request here
        Log.e("path",Fpath.split("%3A")[1]);
        DbHandler.putString(getActivity(),"curr_chosen_directory",Fpath.split("%3A")[1].replaceAll("%2F","/").replaceAll("%20"," "));

        if(!DbHandler.getString(getActivity(),"curr_chosen_directory","").equals("")){
            path.setVisibility(View.VISIBLE);
            path.setText("/"+DbHandler.getString(getActivity(),"curr_chosen_directory",""));
        }
        else{
            path.setText("No folder selected");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
