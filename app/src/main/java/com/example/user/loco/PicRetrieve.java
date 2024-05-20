package com.example.user.loco;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PicRetrieve extends Activity {
    public static ProgressBar progbar;
    public static TextView pre;
    public static boolean picAlive;
    public static Context conPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_retrieve);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        progbar = (ProgressBar) findViewById(R.id.progressBar);
        pre = (TextView)findViewById(R.id.precTextV);
        conPic = this;
        Button imdone = (Button)findViewById(R.id.imdonebtn);
        imdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (Build.VERSION.SDK_INT>=21)
        {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));
        }

    }
    public static void changeProg(int prog)
    {
        if (progbar!=null) {
            progbar.setProgress(prog);
        }
        if (pre!=null)
        {
            String num = String.valueOf(prog);
            pre.setText(num+"%");
        }
    }

    public void onBackPressed() {//when user is pressing back button the app wont go back to login screen
        // TODO Auto-generated method stub
        Toast.makeText(this, "Please Be Patient If It Takes Too Long There Might Be a Problem With Our Server So Try Again Later", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        picAlive = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        picAlive = false;
    }

}
