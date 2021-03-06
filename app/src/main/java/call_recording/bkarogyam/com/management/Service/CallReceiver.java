package call_recording.bkarogyam.com.management.Service;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.CallLog;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import call_recording.bkarogyam.com.management.Activity.FeedbackActivity;
import call_recording.bkarogyam.com.management.Config.DbHandler;
import call_recording.bkarogyam.com.management.JSONBody.IncomingBody;
import call_recording.bkarogyam.com.management.Models.IncomingPOJO;
import call_recording.bkarogyam.com.management.Networking.ServiceGenerator;
import call_recording.bkarogyam.com.management.Requests.IncomingRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by satyam on 19/4/18.
 */

public class CallReceiver extends PhoneCallReceiver {

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {

    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {

    }

    @Override
    protected void onIncomingCallEnded(final Context ctx, String number, Date start, Date end) {
        final String time=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        number=number.replace("+91","");
        final String fname = "BKInNum_" + number +"_"+time+ ".amr";
        String path = Environment.getExternalStorageDirectory().toString() + "/" + DbHandler.getString(ctx, "curr_chosen_directory", "");
        final File dir2 = new File(path);

        if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
            File[] files = dir2.listFiles();

            for (File file : files) {
                if (file.getName().toLowerCase().contains(number)) {
                    File f2 = new File(dir2, fname);
                    Log.e("rename","req_ok");

                    file.renameTo(f2);
                    break;
                }
            }
        }
        else{
            File[] files = dir2.listFiles();
            int flg=0;

            for (File file : files) {
                if (file.getName().toLowerCase().contains(number) && file.isDirectory()) {
                    flg = 1;
                    path=path+"/"+file.getName();
                    break;
                }
            }

            if(flg==1) {
                File dir3 = new File(path);
                File[] files2 = dir3.listFiles();

                File actual_file = null;
                if (files2.length > 0) {
                    actual_file = files2[0];
                    for (int i = 1; i < files2.length; i++) {
                        if (files2[i].lastModified() > actual_file.lastModified()) {
                            actual_file = files2[i];
                        }
                    }
                    File f2 = new File(dir2, fname);
                    actual_file.renameTo(f2);

                }
            }
        }

        Log.e("rename","req_enter");
        IncomingBody incomingBody=new IncomingBody(number,"I");
        IncomingRequest incomingRequest= ServiceGenerator.createService(IncomingRequest.class,DbHandler.getString(ctx,"bearer",""));
        Call<IncomingPOJO> call1=incomingRequest.call(incomingBody);
        final String finalPath = path;
        call1.enqueue(new Callback<IncomingPOJO>() {
            @Override
            public void onResponse(Call<IncomingPOJO> call, Response<IncomingPOJO> response) {
                if(response.code()==200){

                    String new_fname =  "BKIn_" + response.body().getCall_id() +"_"+time+ ".amr";
                    File f2 = new File(dir2,fname);
                    File f3 = new File(dir2,new_fname);
                    f2.renameTo(f3);

                    Log.e("rename",new_fname);

                    Intent intent = new Intent(ctx, FeedbackActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("call_id",response.body().getCall_id());
                    intent.putExtra("fname",new_fname);
                    ctx.startActivity(intent);
                }
                else if (response.code()==403){
                    Toast.makeText(ctx,"Not Authorized",Toast.LENGTH_LONG).show();
                    DbHandler.unsetSession(ctx,"isforcedLoggedOut");
                }
                else {
                    Toast.makeText(ctx, "Unable to connect to server", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<IncomingPOJO> call, Throwable t) {
                Log.e("error",t.getMessage());
                Toast.makeText(ctx, "Unable to connect to server", Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        number=DbHandler.getString(ctx,"mob_number","");
        int flg=0;
        Log.e("number",number+" hello");
        if(DbHandler.contains(ctx,"app")) {

            if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
                String fname = "BKOut_" + DbHandler.getString(ctx, "call_id", "") +"_"+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date())+ ".amr";
                String path = Environment.getExternalStorageDirectory().toString() + "/" + DbHandler.getString(ctx, "curr_chosen_directory", "");
                File dir2 = new File(path);
                File[] files = dir2.listFiles();

                for (File file : files) {
                    if (file.getName().toLowerCase().contains(number)) {
                        flg = 1;
                        File f2 = new File(dir2, fname);
                        file.renameTo(f2);
                    }
                }


                String queryString = "NUMBER=" + number;
                if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                ctx.getContentResolver().delete(CallLog.Calls.CONTENT_URI, queryString, null);

                if (flg == 0) {
                    Toast.makeText(ctx, "Unable to record call. Please start auto call recorder", Toast.LENGTH_LONG).show();

                } else {
                    Intent intent = new Intent(ctx, FeedbackActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("call_id",DbHandler.getString(ctx,"call_id",""));
                    intent.putExtra("fname",fname);
                    ctx.startActivity(intent);
                }
            }
            else{
                String fname = "BKOut_" + DbHandler.getString(ctx, "call_id", "") +"_"+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date())+ ".amr";
                String path = Environment.getExternalStorageDirectory().toString() + "/" + DbHandler.getString(ctx, "curr_chosen_directory", "").replace("%20"," ");
                File dir2 = new File(path);
                File[] files = dir2.listFiles();

                for (File file : files) {
                    if (file.getName().toLowerCase().contains(number) && file.isDirectory()) {
                        flg = 1;
                        path=path+"/"+file.getName();
                        break;
                    }
                }

                if(flg==1) {
                    File dir3 = new File(path);
                    File[] files2 = dir3.listFiles();

                    File actual_file=null;
                    if(files2.length>0){
                        actual_file=files2[0];
                        for(int i=1;i<files2.length;i++){
                            if(files2[i].lastModified()>actual_file.lastModified()){
                                actual_file=files2[i];
                            }
                        }
                        File f2 = new File(dir2,fname);
                        actual_file.renameTo(f2);

                        String queryString = "NUMBER=" + number;
                        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        ctx.getContentResolver().delete(CallLog.Calls.CONTENT_URI, queryString, null);

                        Intent intent = new Intent(ctx, FeedbackActivity.class);
                        intent.putExtra("call_id",DbHandler.getString(ctx,"call_id",""));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);

                    }
                    else{
                        Toast.makeText(ctx, "Unable to record call. Please start auto call recorder", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(ctx, "Unable to record call. Please start auto call recorder", Toast.LENGTH_LONG).show();
                }

            }

        }
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
    }

}