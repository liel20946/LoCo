package com.example.user.loco;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.content.Intent;


import java.util.Calendar;


public class HelloService extends Service  {
    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "com.example.user.loco.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;

    /** indicates how to behave if the service is killed */
    int mStartMode;

    /** interface for clients that bind */
    IBinder mBinder;

    /** indicates whether onRebind should be used */
    boolean mAllowRebind;

    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        super.onCreate();

    }

    /** The service is starting, due to a call to startService() */

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (TCPClient.socket!=null)
        {
            try {
                TCPClient.socket.close();
            }catch (Exception a){}

        }

        new connectTask(this).execute("");//starting the async task



        return mStartMode;
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {
    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {


        if (Build.VERSION.SDK_INT>=23)
        {
            Intent myIntent = new Intent(this, HelloService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 1);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,Calendar.getInstance().getTimeInMillis()+5000,pendingIntent);
        }

    }

    public  static void firstTouch()//when the client creates first contact with the server we do that
    {

        String ends;
        TinyDB tdb = new TinyDB(MainActivity.cpn);
        boolean logS = tdb.getBoolean("isLoged");
        if(MainActivity.cpn==null)
        {
            //sholdnt happen but just in case
            ends = "100@a,a*";//have to initialize ends anyway
        }
        else
        {
            WifiManager wifiManager = (WifiManager) MainActivity.cpn.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress = wInfo.getMacAddress();
            new User(MainActivity.cpn);
            ends = "100@"+User.username+","+User.password+","+macAddress+"*";
        }


        if (MainActivity.mTcpClient != null) {
            if (logS)
            {
                MainActivity.mTcpClient.sendMessage(ends);
            }

        }

        //sends the message to the server

    }

    /***************************************************************************************/
    /********************************
     * Messages from server
     ***************************************/

    public static class connectTask extends AsyncTask<String, String, TCPClient> {

        Context context;
        private connectTask(Context context) {
            this.context = context.getApplicationContext();
        }


        @Override
        protected TCPClient doInBackground(String... message) {
            if(MainActivity.cpn==null)//if the app is closed and main was not created
            {
                MainActivity.cpn = context;

            }

            //we create a TCPClient object and
            MainActivity.mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    /*
                    */
                    publishProgress(message);
                }
            });


            MainActivity.mTcpClient.run();



            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {


            super.onProgressUpdate(values);
            Communication.ManageData(values[0],context);//just to be safe im sending the context

        }

    }




}