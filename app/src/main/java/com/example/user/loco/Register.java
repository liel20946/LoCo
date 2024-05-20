package com.example.user.loco;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Register extends ActionBarActivity {

    private ArrayList<String> infoArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(20);

        TinyDB tinydb = new TinyDB(this);
        toolbar.setBackgroundColor(getResources().getColor(R.color.appBarC));
        int accent =  tinydb.getInt("colorAc");
        toolbar.setTitleTextColor(getResources().getColor(accent));

        infoArray = new ArrayList<String>();

        EditText password = (EditText) findViewById(R.id.PassRS);
        password.setTransformationMethod(new PasswordTransformationMethod());

        EditText answer = (EditText) findViewById(R.id.IdNum);
        final Button regbtn = (Button) findViewById(R.id.Registerbtn);

        EditText user = (EditText) findViewById(R.id.UserRS);
        EditText name = (EditText) findViewById(R.id.FnameRS);
        EditText lname = (EditText) findViewById(R.id.LnameRS);
        EditText age = (EditText) findViewById(R.id.AgeRS);
        EditText adress = (EditText) findViewById(R.id.HomeRS);
        EditText answer2 = (EditText) findViewById(R.id.AnswerRS);

        regbtn.setBackgroundColor(getResources().getColor(accent));
        password.setTextColor(getResources().getColor(accent));
        password.setHintTextColor(getResources().getColor(accent));

        answer.setTextColor(getResources().getColor(accent));
        answer.setHintTextColor(getResources().getColor(accent));

        user.setTextColor(getResources().getColor(accent));
        user.setHintTextColor(getResources().getColor(accent));

        name.setTextColor(getResources().getColor(accent));
        name.setHintTextColor(getResources().getColor(accent));

        lname.setTextColor(getResources().getColor(accent));
        lname.setHintTextColor(getResources().getColor(accent));

        age.setTextColor(getResources().getColor(accent));
        age.setHintTextColor(getResources().getColor(accent));

        adress.setTextColor(getResources().getColor(accent));
        adress.setHintTextColor(getResources().getColor(accent));

        answer2.setTextColor(getResources().getColor(accent));
        answer2.setHintTextColor(getResources().getColor(accent));

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.appc));
            int color = getResources().getColor(R.color.appBarC);
            answer.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            answer2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            name.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            lname.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            age.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            adress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            user.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, bm, color);

            setTaskDescription(td);
            bm.recycle();
        }
        //when the user click the im done button on keyboard
        answer.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    regbtn.performClick();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(regbtn.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void RegisterF(View v) {
        String User, Pass, Name, Lname, Age, Adress,Answer,ends,Id;
        boolean unRecChars = false;
        boolean unRecPas = false;
        int i,j;

        EditText user = (EditText) findViewById(R.id.UserRS);
        EditText pass = (EditText) findViewById(R.id.PassRS);
        EditText name = (EditText) findViewById(R.id.FnameRS);
        EditText lname = (EditText) findViewById(R.id.LnameRS);
        EditText age = (EditText) findViewById(R.id.AgeRS);
        EditText adress = (EditText) findViewById(R.id.HomeRS);
        EditText answer = (EditText) findViewById(R.id.AnswerRS);
        EditText id = (EditText) findViewById(R.id.IdNum);

        User = user.getText().toString();
        Pass = pass.getText().toString();
        Name = name.getText().toString();
        Lname = lname.getText().toString();
        Age = age.getText().toString();
        Adress = adress.getText().toString();
        Answer = answer.getText().toString();
        Id = id.getText().toString();

        int ttt=0;
        try{
            ttt=Integer.valueOf(Age);
        } catch (Exception a){}
        boolean idPas=true;
               boolean agePas = false,empty=false;

        if(Adress==""||Adress==null)
        {
            empty =true;
        }

        if (Id.length()==9)if(idVerification(Id)) idPas=false;

        if (ttt<0||ttt>150)
        {
            agePas=true;
        }

        for(i=0;i<User.length();i++)
        {

            if(User.charAt(i)=='*'||User.charAt(i)==',')
            {
                unRecChars = true;
            }
        }

        for(j=0;j<Pass.length();j++)
        {
            if(Pass.charAt(j)=='*'||Pass.charAt(j)==',')
            {
                unRecPas = true;
            }
        }

        if(User.length()>12||unRecChars||unRecPas||agePas||empty||idPas)
        {

            if (idPas)
            {
                Toast.makeText(this, "Your id is Not Valid", Toast.LENGTH_SHORT).show();
            }

             if (User.length()>12||unRecChars||unRecPas)
            {
                Toast.makeText(this, "User Name Cannot Be Longer Than 12 Characters", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "You Cant Use * Or , in User Name Or Password", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Your Age is Not Valid", Toast.LENGTH_SHORT).show();
            }

        }

        else
        {
            if(!Answer.equals(""))
            {
                Answer = Answer.replace('@','^');
            }


            infoArray.add(User);
            infoArray.add(Pass);
            infoArray.add(Name);
            infoArray.add(Lname);
            infoArray.add(Age);
            infoArray.add(Adress);
            infoArray.add(Answer);

            WifiManager wifiManager = (WifiManager) MainActivity.cpn.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress = wInfo.getMacAddress();
            infoArray.add(macAddress);

            ends = Communication.pattern(infoArray, 102);


            //sends the message to the server
            if (MainActivity.mTcpClient != null) {

                MainActivity.mTcpClient.sendMessage(ends);
            }

        }


        finish();

    }

    public static boolean idVerification(String id) {
        int sum=0, i, num;
        boolean pass=false;
        for(i=0; i<9; i++)
        {
            if(i%2==0)
            {
                num = Character.getNumericValue(id.charAt(i));
                num= num/10+ num-((num/10)*10);
                sum +=num;
            }
            else
            {
                num = Character.getNumericValue(id.charAt(i))*2;
                num= num/10+ num-((num/10)*10);
                sum +=num;
            }
        }
        if(sum%10==0)pass =true;
        return pass;
    }



}
