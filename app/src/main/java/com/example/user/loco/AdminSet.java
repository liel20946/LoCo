package com.example.user.loco;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminSet extends ActionBarActivity {
    public static Activity adminCpn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_set);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        adminCpn = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Group Settings");
        setSupportActionBar(toolbar);
        final TinyDB tdb = new TinyDB(this);
        String Pass = tdb.getString(TabFragment1.currentChatName+"pass");
        String time = tdb.getString(TabFragment1.currentChatName+"time");

        Button adminbtn = (Button) findViewById(R.id.adminDone);
        final EditText name = (EditText) findViewById(R.id.GNameAdmin);
        final EditText pass = (EditText) findViewById(R.id.GPassAdmin);
        TextView TTL = (TextView)findViewById(R.id.currentTTL);

        TTL.setText("Current Days Remaining: "+time);
        name.setText(TabFragment1.currentChatName);
        pass.setText(Pass);

        int accent =  tdb.getInt("colorAc");



        TTL.setTextColor(getResources().getColor(accent));
        name.setTextColor(getResources().getColor(accent));
        pass.setTextColor(getResources().getColor(accent));




        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.appc));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));
            int color = getResources().getColor(R.color.appBarC);

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, bm, color);

            setTaskDescription(td);
            bm.recycle();
        }


        Spinner dropdown = (Spinner)findViewById(R.id.ADMINTTL);
        String[] items = new String[]{"1 Day", "3 Days","Week","Month","6 Month","Year","Unlimited"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                switch (position) {
                    case 0:
                        tdb.putString("AdminGroupTime","1");
                        break;
                    case 1:
                        tdb.putString("AdminGroupTime","3");
                        break;
                    case 2:
                        tdb.putString("AdminGroupTime","7");
                        break;
                    case 3:
                        tdb.putString("AdminGroupTime","30");
                        break;
                    case 4:
                        tdb.putString("AdminGroupTime","180");
                        break;
                    case 5:
                        tdb.putString("AdminGroupTime","365");
                        break;
                    case 6:
                        tdb.putString("AdminGroupTime","999");
                        break;
                    default:
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(20);
        final Context ccs= this;

        adminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ttl = tdb.getString("AdminGroupTime");
                ArrayList<String> abc = new ArrayList<String>();
                new User(ccs);
                abc.add(User.username);
                abc.add(ttl);
                abc.add(TabFragment1.currentChatName);
                abc.add(name.getText().toString());
                abc.add(pass.getText().toString());

                String donzo = Communication.pattern(abc,905);
                if (MainActivity.mTcpClient!=null)
                {
                    MainActivity.mTcpClient.sendMessage(donzo);
                }
                finish();
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

    public static void FinishAdmin()
    {
        if (adminCpn!=null)
        {
            try
            {
                adminCpn.finish();

            }catch (Exception a){}

        }

    }
}
