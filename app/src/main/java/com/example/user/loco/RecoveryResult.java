package com.example.user.loco;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RecoveryResult extends ActionBarActivity {

    public static String PassRecovery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Recovery Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(20);

        TinyDB tinydb = new TinyDB(this);

        toolbar.setBackgroundColor(getResources().getColor(R.color.appBarC));
        int accent =  tinydb.getInt("colorAc");
        toolbar.setTitleTextColor(getResources().getColor(accent));


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.appc));
            int color = getResources().getColor(R.color.appBarC);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, bm, color);

            setTaskDescription(td);
            bm.recycle();
        }

        TextView HP = (TextView)findViewById(R.id.passwordHide);
        TextView lp = (TextView)findViewById(R.id.textView2);

        HP.setText(PassRecovery);
        HP.setTextColor(getResources().getColor(accent));
        lp.setTextColor(getResources().getColor(accent));



        Button btn = (Button)findViewById(R.id.showThePassBtn);
        Button cpy = (Button)findViewById(R.id.copybtn);

        btn.setBackgroundColor(getResources().getColor(accent));
        cpy.setBackgroundColor(getResources().getColor(accent));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView SP = (TextView)findViewById(R.id.passwordShow);
                SP.setText(PassRecovery);

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


    public void Copy(View v)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("a", PassRecovery);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(MainActivity.cpn, "Password Was Copied", Toast.LENGTH_SHORT).show();
    }
}
