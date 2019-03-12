package call_recording.jbglass.in.callrecording.Service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;

import java.util.List;
import java.util.Random;

import call_recording.jbglass.in.callrecording.Activity.FeedbackActivity;
import call_recording.jbglass.in.callrecording.Activity.MainActivity;
import call_recording.jbglass.in.callrecording.Config.DbHandler;
import call_recording.jbglass.in.callrecording.R;

/**
 * Created by satyam on 16/6/18.
 */

public class AlarmReceiver extends BroadcastReceiver {
    int item = 0;// 0 for sim1 & 1 for sim2
    private List<PhoneAccountHandle> phoneAccountHandleList;
    private final static String simSlotName[] = {
            "extra_asus_dial_use_dualsim",
            "com.android.phone.extra.slot",
            "slot",
            "simslot",
            "sim_slot",
            "subscription",
            "Subscription",
            "phone",
            "com.android.phone.DialingMode",
            "simSlot",
            "slot_id",
            "simId",
            "simnum",
            "phone_type",
            "slotId",
            "slotIdx"
    };

    @Override
    public void onReceive(Context context, Intent intent2) {

        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("number",intent2.getExtras().getString("mob_no"));
        notificationIntent.putExtra("action","call");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        long[] pattern = {500,500,500,500,500,500,500,500,500};

        Notification notification = builder.setContentTitle("Calling App")
                .setContentText("Call "+intent2.getExtras().getString("title"))
                .setTicker("New Message Alert!")
                .setSound(alarmSound)
                .setOnlyAlertOnce(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE,500,500)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}