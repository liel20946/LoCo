package com.example.user.loco;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;


public class MyBroadcastReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        if (Build.VERSION.SDK_INT>=23)
        {
                Intent myIntent = new Intent(context, MyBroadcastReciver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.SECOND, 1);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,Calendar.getInstance().getTimeInMillis()+50000,pendingIntent);
        }


        context.startService(new Intent(context ,HelloService.class));

    }

}
