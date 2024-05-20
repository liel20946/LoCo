package com.example.user.loco;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;


import java.util.ArrayList;


public class Settings extends ActionBarActivity {

    public static ArrayList<String> Me;
    public static Context SettingCpn;
    public static SetAdapter sAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SettingCpn = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(20);
        final TinyDB tdb = new TinyDB(this);


        toolbar.setBackgroundColor(getResources().getColor(R.color.appBarC));
        int accent = tdb.getInt("colorAc");
        toolbar.setTitleTextColor(getResources().getColor(accent));


        ListView tList = (ListView) findViewById(R.id.SetList);

        sAdapter = new SetAdapter(this);//sending the saved list of chats(users)
        sAdapter.addMessage(0, "  Notifications");
        sAdapter.addMessage(1, "  Vibration");
        sAdapter.addMessage(2, "  Color");
        sAdapter.addMessage(3, "  Delete Account");
        tList.setAdapter(sAdapter);


        tList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                switch (position) {
                    case 0:
                        break;

                    case 1:
                        break;

                    case 2:
                        ChangeAccentColor();
                        break;

                    case 3:
                        deleteAccount();
                        break;

                    default:

                }

            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.appc));
            int color = getResources().getColor(R.color.appBarC);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, bm, color);

            setTaskDescription(td);
            bm.recycle();
        }

        //toggle setup and behavior


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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

    public void deleteAccount() {
        //starting a dialog for the user
        final CharSequence[] items = {"Yes I Am Sure...", "Nope My Bad..."};

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.YourAlertDialogTheme);
        builder.setTitle("Are You Sure You Want To Delete Your Account?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {

                    case 0:
                        Me = new ArrayList<String>();
                        String me, done;
                        new User(MainActivity.cpn);
                        me = User.username;

                        Me.add(me);

                        done = Communication.pattern(Me, 104);
                        //sends the message to the server
                        if (MainActivity.mTcpClient != null) {

                            MainActivity.mTcpClient.sendMessage(done);
                        }

                        break;

                    case 1:
                        break;

                    default:

                }

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void ChangeAccentColor() {

        final CharSequence[] items3 = {"Restart"};
        final AlertDialog.Builder builder3 = new AlertDialog.Builder(SettingCpn, R.style.YourAlertDialogTheme);
        builder3.setTitle("You Have To Restart The App");
        builder3.setItems(items3, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item3) {
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


        final CharSequence[] items2 = {"red", "purple", "ultra green", "yellow", "black", "green", "blue", "ultra pink", "default"};
        AlertDialog.Builder builder2 = new AlertDialog.Builder(SettingCpn, R.style.YourAlertDialogTheme);
        builder2.setTitle("What Accent Color Do You Want?");
        builder2.setItems(items2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item2) {

                AlertDialog alert3 = builder3.create();
                switch (item2) {
                    case 0:
                        Colors.ChangeAccent("r");
                        break;
                    case 1:
                        Colors.ChangeAccent("p");
                        break;
                    case 2:
                        Colors.ChangeAccent("g");
                        break;
                    case 3:
                        Colors.ChangeAccent("y");
                        break;

                    case 4:
                        Colors.ChangeAccent("c");
                        break;
                    case 5:
                        Colors.ChangeAccent("b");
                        break;
                    case 6:
                        Colors.ChangeAccent("gg");
                        break;
                    case 7:
                        Colors.ChangeAccent("pp");
                        break;
                    case 8:
                        Colors.ChangeAccent("df");
                        break;

                    default:
                }

                alert3.show();
            }

        });
        AlertDialog alert2 = builder2.create();
        alert2.show();

    }


}
