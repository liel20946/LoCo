package com.example.user.loco;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.ColorStateList;
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

import java.util.ArrayList;


public class Recovery extends ActionBarActivity {

    public static ArrayList<String> recoveryArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Recover Information");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(20);


        TinyDB tinydb = new TinyDB(this);


        toolbar.setBackgroundColor(getResources().getColor(R.color.appBarC));
        int accent = tinydb.getInt("colorAc");
        toolbar.setTitleTextColor(getResources().getColor(accent));

        EditText answer = (EditText) findViewById(R.id.RecAns);
        EditText un = (EditText) findViewById(R.id.RecUser);
        final Button recbtn = (Button) findViewById(R.id.RecBtn);


        recbtn.setBackgroundColor(getResources().getColor(accent));
        un.setTextColor(getResources().getColor(accent));
        un.setHintTextColor(getResources().getColor(accent));
        answer.setHintTextColor(getResources().getColor(accent));
        answer.setTextColor(getResources().getColor(accent));


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));
            getWindow().setStatusBarColor(getResources().getColor(R.color.appc));
            answer.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            un.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));

            int color = getResources().getColor(R.color.appBarC);

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
                    recbtn.performClick();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(recbtn.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }

    public void RecDone(View v) {

        recoveryArr = new ArrayList<String>();
        String n, a, done;

        EditText name = (EditText) findViewById(R.id.RecUser);
        EditText answer = (EditText) findViewById(R.id.RecAns);

        n = name.getText().toString();
        a = answer.getText().toString();

        if (!a.equals(""))
        {
            a = a.replace('@','^');
        }

        recoveryArr.add(n);
        recoveryArr.add(a);

        done = Communication.pattern(recoveryArr, 103);

        //sends the message to the server
        if (MainActivity.mTcpClient != null) {

            MainActivity.mTcpClient.sendMessage(done);
        }

        finish();
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
