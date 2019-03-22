package call_recording.bkarogyam.com.management.Config;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.Gson;

import call_recording.bkarogyam.com.management.Activity.AdminActivity;
import call_recording.bkarogyam.com.management.Activity.MainActivity;
import call_recording.bkarogyam.com.management.Activity.ManagerActivity;
import call_recording.bkarogyam.com.management.Activity.StartActivity;
import call_recording.bkarogyam.com.management.Models.MemberInfoPOJO;
import call_recording.bkarogyam.com.management.Networking.ServiceGenerator;
import call_recording.bkarogyam.com.management.Requests.MemberInfoRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DbHandler {

    public static void putInt(Context context, String Key, int value) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(Key, value);
            editor.commit();
        }
    }

    public static void putString(Context context, String Key, String value) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Key, value);
            editor.commit();
        }
    }

    public static void putBoolean(Context context, String Key, Boolean value) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Key, value);
            editor.commit();
        }
    }

    public static Boolean contains(Context context,String key){
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            return prefs.contains(key);
        } else return null;
    }

    public static int getInt(Context context, String Key, int Alternate) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);

            return prefs.getInt(Key, Alternate);
        } else return 0;
    }

    public static String getString(Context context, String Key, String Alternate) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            return prefs.getString(Key, Alternate);
        } else return null;
    }

    public static Boolean getBoolean(Context context, String Key, Boolean Alternate) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            return prefs.getBoolean(Key, Alternate);
        } else return false;
    }
    public static void remove(Context context,String key){
        if(context!=null){
            SharedPreferences prefs;
            prefs=context.getSharedPreferences(Config.DB_NAME,Context.MODE_PRIVATE);
            if(DbHandler.contains(context,key)) {
                prefs.edit().remove(key).commit();
            }
        }
    }

    public static void clearDb(Context context) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(Config.DB_NAME, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
        }
    }

    public static void setSession(final Context context, final String bearer, final String user_type) {
        if (context != null) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);

            DbHandler.putString(context, "bearer", bearer);
            DbHandler.putString(context, "user_type", user_type);
            DbHandler.putBoolean(context, "isLoggedIn", true);

            MemberInfoRequest memberInfoRequest = ServiceGenerator.createService(MemberInfoRequest.class, DbHandler.getString(context, "bearer", ""));
            Call<MemberInfoPOJO> memberInfoPOJOCall = memberInfoRequest.call();
            memberInfoPOJOCall.enqueue(new Callback<MemberInfoPOJO>() {
                @Override
                public void onResponse(Call<MemberInfoPOJO> call, Response<MemberInfoPOJO> response) {

                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        Intent intent;

//                        if(!user_type.equals(response.body().getData().getEmpType())){
//                            Toast.makeText(context, "Not Authorized", Toast.LENGTH_LONG).show();
//                            DbHandler.unsetSession(context, "isforcedLoggedOut");
//                        }
                        //else {
                            if (user_type.equals("employee")) {
                                intent = new Intent(context, MainActivity.class);
                            } else if (user_type.equals("manager")) {
                                intent = new Intent(context, ManagerActivity.class);
                            } else {
                                intent = new Intent(context, AdminActivity.class);
                            }
                            DbHandler.putString(context, "member_info", new Gson().toJson(response.body().getData()));

                            intent.putExtra("frag", "d");
                            intent.putExtra("action", "intent");

                            context.startActivity(intent);
                            ((Activity) context).finishAffinity();
                       // }
                    } else if (response.code() == 403) {
                        progressDialog.dismiss();

                        Toast.makeText(context, "Not Authorized", Toast.LENGTH_LONG).show();
                        DbHandler.unsetSession(context, "isforcedLoggedOut");
                    } else {
                        progressDialog.dismiss();
                        new AlertDialog.Builder(context).setTitle("Error").setMessage("Unable to connect to server")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).create().show();
                    }
                }

                @Override
                public void onFailure(final Call<MemberInfoPOJO> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("terror",t.getMessage());
                    new AlertDialog.Builder(context).setTitle("error").setMessage("Unable to connect to server")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();

                }
            });

        }
    }

    public static void unsetSession(Context context, String type) {
        if (context != null) {
            DbHandler.clearDb(context);
            context.startActivity(new Intent(context, StartActivity.class));
            ((Activity) context).finishAffinity();
            Toast.makeText(context,"You have been logged out successfully",Toast.LENGTH_LONG).show();
        }
    }
}