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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateGroup extends ActionBarActivity {
    public static  EditText GnameC;
    public static  EditText GPassC;
    public static ArrayList<String> CreateArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Create A Group");
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(20);

        TinyDB tinydb = new TinyDB(this);
        toolbar.setBackgroundColor(getResources().getColor(R.color.appBarC));
        int accent =  tinydb.getInt("colorAc");
        toolbar.setTitleTextColor(getResources().getColor(accent));

        GnameC = (EditText) findViewById(R.id.GroupNameC);
        GPassC = (EditText) findViewById(R.id.GroupPassC);
        final TinyDB tdb = new TinyDB(this);
        Spinner dropdown = (Spinner)findViewById(R.id.spinner111);
        String[] items = new String[]{"1 Day", "3 Days","Week","Month","6 Month","Year","Unlimited"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                switch (position) {
                    case 0:
                        tdb.putString("CreateGroupTime","1");
                        break;
                    case 1:
                        tdb.putString("CreateGroupTime","3");
                        break;
                    case 2:
                        tdb.putString("CreateGroupTime","7");
                        break;
                    case 3:
                        tdb.putString("CreateGroupTime","30");
                        break;
                    case 4:
                        tdb.putString("CreateGroupTime","180");
                        break;
                    case 5:
                        tdb.putString("CreateGroupTime","365");
                        break;
                    case 6:
                        tdb.putString("CreateGroupTime","999");
                        break;
                    default:
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final Button Join = (Button) findViewById(R.id.CreateBtnG);
        Join.setBackgroundColor(getResources().getColor(accent));
        GnameC.setTextColor(getResources().getColor(accent));
        GnameC.setHintTextColor(getResources().getColor(accent));
        GPassC.setTextColor(getResources().getColor(accent));
        GPassC.setHintTextColor(getResources().getColor(accent));
        dropdown.setBackgroundColor(getResources().getColor(accent));

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.appc));
            int color = getResources().getColor(R.color.appBarC);
            GnameC.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            GPassC.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(accent)));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, bm, color);

            setTaskDescription(td);
            bm.recycle();
        }

        GPassC.setOnEditorActionListener(new EditText.OnEditorActionListener() {
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

    public  void CreateAGroup(View v)
    {
        TinyDB tdb = new TinyDB(this);
        String ttl = tdb.getString("CreateGroupTime");
        String name,pas,ToSend;
        name = GnameC.getText().toString();
        pas=GPassC.getText().toString();
        CreateArr = new ArrayList<String>();
        CreateArr.add(name);
        CreateArr.add(pas);
        new User(MainActivity.cpn);
        CreateArr.add(User.username);
        CreateArr.add(ttl);

        tdb.putBoolean(name+"isAdmin",true);
        tdb.putString(name+"pass",pas);
        tdb.putString(name+"time",ttl);

        ToSend = Communication.pattern(CreateArr,900);
        if (MainActivity.mTcpClient!=null)
        {
            MainActivity.mTcpClient.sendMessage(ToSend);
        }
        CreateArr.clear();
        finish();
    }
}
