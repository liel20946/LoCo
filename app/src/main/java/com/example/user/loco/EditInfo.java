package com.example.user.loco;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class EditInfo extends ActionBarActivity {

    public static ArrayList<String> infoArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Edit Information");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(20);
        TinyDB tinydb = new TinyDB(this);
        toolbar.setBackgroundColor(getResources().getColor(R.color.appBarC));
        int accent = tinydb.getInt("colorAc");
        toolbar.setTitleTextColor(getResources().getColor(accent));


        EditText name = (EditText) findViewById(R.id.fnEdit);
        EditText last = (EditText) findViewById(R.id.lnEdit);
        EditText age = (EditText) findViewById(R.id.agEdit);
        EditText adress = (EditText) findViewById(R.id.adEdit);
        EditText password = (EditText) findViewById(R.id.psEdit);

        name.setTextColor(getResources().getColor(accent));
        last.setTextColor(getResources().getColor(accent));
        age.setTextColor(getResources().getColor(accent));
        adress.setTextColor(getResources().getColor(accent));
        password.setTextColor(getResources().getColor(accent));


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.appc));
            int color = getResources().getColor(R.color.appBarC);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, bm, color);

            setTaskDescription(td);
            bm.recycle();
        }

        new User(this);

        name.setText(User.name);
        last.setText(User.lastname);
        age.setText(User.age);
        adress.setText(User.adress);
        password.setText(User.password);


        final Button recbtn = (Button) findViewById(R.id.SaveInfo);

        recbtn.setBackgroundColor(getResources().getColor(accent));

        //when the user click the im done button on keyboard
        adress.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    recbtn.performClick();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(recbtn.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }


    public void saveInfo(View v) {
        TinyDB tdb = new TinyDB(this);
        boolean agePas=false,len=true,Pas=true;
        int ttt=-1;

        EditText name = (EditText) findViewById(R.id.fnEdit);
        EditText last = (EditText) findViewById(R.id.lnEdit);
        EditText age = (EditText) findViewById(R.id.agEdit);
        EditText adress = (EditText) findViewById(R.id.adEdit);
        EditText password = (EditText) findViewById(R.id.psEdit);

        String na, la, ag, ad, end,ps;

        na = name.getText().toString();
        la = last.getText().toString();
        ag = age.getText().toString();
        ad = adress.getText().toString();
        ps = password.getText().toString();
        int j;

        try{
            ttt=Integer.valueOf(ag);
        } catch (Exception a){}

        if (na.length()>12)len=false;
        if (la.length()>12)len=false;
        if (ag.length()>12)len=false;
        if (ad.length()>12)len=false;

        if (ttt>0&&ttt<150)
        {
            agePas=true;
        }
        for(j=0;j<ps.length();j++)
        {
            if(ps.charAt(j)=='*'||ps.charAt(j)==',')
            {
                Pas = false;
            }
        }

        if (agePas&&len&&Pas)
        {
            tdb.putString("TheName", na);
            tdb.putString("TheUserLast", la);
            tdb.putString("TheUserAge", ag);
            tdb.putString("TheUserAdress", ad);
            tdb.putString("TheUserPass",ps);

            infoArr = new ArrayList<String>();

            infoArr.add(User.username);
            infoArr.add("firstName");
            infoArr.add(na);

            infoArr.add("lastName");
            infoArr.add(la);

            infoArr.add("age");
            infoArr.add(ag);

            infoArr.add("loc");
            infoArr.add(ad);

            infoArr.add("password");
            infoArr.add(ps);

            end = Communication.pattern(infoArr, 500);

            //sends the message to the server
            if (MainActivity.mTcpClient != null) {

                MainActivity.mTcpClient.sendMessage(end);
            }
        }
        else
        {
            Toast.makeText(this, "Error in Details", Toast.LENGTH_SHORT).show();
        }


        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
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


}
