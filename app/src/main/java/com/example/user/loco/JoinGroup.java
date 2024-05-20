package com.example.user.loco;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

public class JoinGroup extends ActionBarActivity {
    public static  EditText Gname;
    public static  EditText GPass;
    public static ArrayList<String> JoinArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Join A Group");
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(20);

        TinyDB tinydb = new TinyDB(this);
        toolbar.setBackgroundColor(getResources().getColor(R.color.appBarC));
        int accent =  tinydb.getInt("colorAc");
        toolbar.setTitleTextColor(getResources().getColor(accent));

        Gname = (EditText) findViewById(R.id.GroupNameC);
        GPass = (EditText) findViewById(R.id.GroupPassC);

        final Button Join = (Button) findViewById(R.id.CreateBtnG);
        Join.setBackgroundColor(getResources().getColor(accent));
        Gname.setTextColor(getResources().getColor(accent));
        Gname.setHintTextColor(getResources().getColor(accent));
        GPass.setTextColor(getResources().getColor(accent));
        GPass.setHintTextColor(getResources().getColor(accent));

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.appc));
            int color = getResources().getColor(R.color.appBarC);
            Gname.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            GPass.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, bm, color);

            setTaskDescription(td);
            bm.recycle();
        }

        GPass.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Join.performClick();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(Join.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

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

    public  void JoingAGroup(View v)
    {
        String name,pas,ToSend;
        name = Gname.getText().toString();
        pas=GPass.getText().toString();
        JoinArr = new ArrayList<String>();
        JoinArr.add(name);
        JoinArr.add(pas);
        new User(MainActivity.cpn);
        JoinArr.add(User.username);

        ToSend = Communication.pattern(JoinArr,901);
        if (MainActivity.mTcpClient!=null)
        {
            MainActivity.mTcpClient.sendMessage(ToSend);
        }
        JoinArr.clear();
        finish();
    }
}
