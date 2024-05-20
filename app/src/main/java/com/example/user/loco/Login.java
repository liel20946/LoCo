package com.example.user.loco;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class Login extends Activity {


    private ArrayList<String> theArrayL;

    EditText username;
    EditText password;

    String user = "";
    String pass = "";
    boolean loged = false;
    public static TextView errotMessage;
    public static Context LogCpn;
    public static ProgressBar spnbar;
    public static boolean LogActive;
    public static  Button loginbtn;

    String endString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_login_system);
        spnbar =(ProgressBar)  findViewById(R.id.spiningBar);

        theArrayL = new ArrayList<String>();
        LogCpn = this;

        TinyDB tinydb = new TinyDB(this);

        errotMessage = (TextView)findViewById(R.id.loginstate);

        loginbtn = (Button) findViewById(R.id.log);
        Button regsbtnn = (Button) findViewById(R.id.button);
        EditText password1 = (EditText) findViewById(R.id.Password);
        EditText uName = (EditText) findViewById(R.id.Username);
        password1.setTransformationMethod(new PasswordTransformationMethod());

        int accent =  tinydb.getInt("colorAc");
        if (accent==0||accent==2131427330)
        {
            accent = R.color.LoCoOrange;
            tinydb.putInt("colorAc",accent);
        }

        loginbtn.setBackgroundColor(getResources().getColor(accent));
        regsbtnn.setBackgroundColor(getResources().getColor(accent));


        if (Build.VERSION.SDK_INT >= 21) {
            int color = getResources().getColor(R.color.appBarC);
            getWindow().setStatusBarColor(color);
            uName.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            password1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));


            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, bm, color);

            setTaskDescription(td);
            bm.recycle();
        }

        View l =  findViewById(R.id.layitout);
        l.setBackgroundColor(getResources().getColor(R.color.appBarC));

        //when the user clicked the im done in keyboard
        password1.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try
                    {
                        loginbtn.performClick();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(loginbtn.getWindowToken(), 0);
                    }catch (Exception a){}


                    return true;
                }
                return false;
            }
        });


        //finishing the activity when user is loged
        BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {

                    TinyDB tinydb = new TinyDB(MainActivity.cpn);
                    boolean killemm = tinydb.getBoolean("Cleaned");
                    if (killemm)
                    {
                        tinydb.putBoolean("Cleaned",false);
                        tinydb.putBoolean("c2",true);
                        new User(MainActivity.cpn);
                        String ddends = "505@"+User.username+"*";
                        if (MainActivity.mTcpClient != null) {
                            MainActivity.mTcpClient.sendMessage(ddends);
                        }
                    }
                    finish();

                }
            }
        };
        registerReceiver(broadcast_reciever, new IntentFilter("finish_activity"));

    }

    @Override
    public void onStart() {
        super.onStart();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LogActive = true;
    }
    @Override
    public void onResume() {
        super.onResume();
        EndSpin();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        LogActive = false;
    }

    //
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    public void LogIn(View v) {
        spnbar.setVisibility(View.VISIBLE);
        username = (EditText) findViewById(R.id.Username);
        password = (EditText) findViewById(R.id.Password);

        user = username.getText().toString();
        pass = password.getText().toString();

        theArrayL.add(user);
        theArrayL.add(pass);
        WifiManager wifiManager = (WifiManager) MainActivity.cpn.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        theArrayL.add(macAddress);
        endString = Communication.pattern(theArrayL, 100);

        new User(MainActivity.cpn);
        TinyDB tinydb = new TinyDB(this);
        String p,u;
        u = tinydb.getString("Userklmn");

        if(User.username.equals(""))
        {

        }
        else if (!user.equals(u))
        {
            tinydb.putBoolean("KillEm",true);
        }
        else
        {
            tinydb.putBoolean("KillEm",false);
        }


        tinydb.putString("TheUserName", user);
        tinydb.putString("TheUserPass", pass);


        loged = true;

        if (MainActivity.mTcpClient!=null)
        {
            MainActivity.mTcpClient.sendMessage(endString);
        }

        theArrayL.clear();


    }

    public void Register(View v) {

        Intent a = new Intent(getApplicationContext(), Register.class);
        startActivity(a);

    }


    public void Forgot(View v)
    {
        Intent c = new Intent(getApplicationContext(),Recovery.class);
        startActivity(c);
    }


    public static void errorLogIn(TextView t,String mes)
    {
        t.setText(mes);

    }
    public static void EndSpin()
    {
        if (spnbar!=null)
        {
            spnbar.setVisibility(View.INVISIBLE);
        }

    }


}









