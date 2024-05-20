package com.example.user.loco;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;


import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.hannesdorfmann.swipeback.transformer.SlideSwipeBackTransformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class In_Chat extends ActionBarActivity {



    public static ArrayList<String> toSendArray;
    public static ArrayList<String> TaRR;
    public static ArrayList<String> idforView;
    public static ArrayList<Integer> Directions;
    public static ArrayList<Integer> Vstate;
    public static ChatCustomAdapter mAdapter2;
    public static ArrayList<String> forGroupM;
    public static boolean InChatActive;
    public static Context InChatCpn;
    public static Activity ChatAc;
    public static Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.active)
        {

            SwipeBack.attach(this, Position.LEFT)
                    .setContentView(R.layout.activity_in__chat)
                    .setSwipeBackContainerBackgroundColor(getResources().getColor(R.color.appBarC))
                    .setSwipeBackTransformer(new SlideSwipeBackTransformer())
                    .setSwipeBackView(R.layout.justcolor);
        }
        else
        {
            setContentView(R.layout.activity_in__chat);
        }

        ChatAc = this;
        setTitle(TabFragment1.currentChatName);
        final TinyDB tinydb = new TinyDB(In_Chat.this);

        InChatCpn = this;
        ListView mList2;

        int i;

        toolbar = (Toolbar) findViewById(R.id.app_bar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(20);
        try {
            if (!TabFragment1.groupChat)
            {
                Bitmap f = getDrawable(TabFragment1.currentChatName);
                Bitmap a = getCroppedBitmap(f,100);
                Drawable d = new BitmapDrawable(getResources(), a);
                if (f!=null&&a!=null){getSupportActionBar().setIcon(d);}
            }

        }catch (Exception aaas){}

        boolean wes = tinydb.getBoolean("WeS");
        if (TabFragment1.Fam!=null&&!wes)
        {
            TabFragment1.Fam.showMenuButton(true);
        }

        mList2 = (ListView) findViewById(R.id.list2);
        mAdapter2 = new ChatCustomAdapter(this);
        mList2.setAdapter(mAdapter2);


        Button send2 = (Button) findViewById(R.id.send_button2);
        final EditText editText2 = (EditText) findViewById(R.id.editText2);


        //getting the data from the client data base (tinydb)


        toolbar.setBackgroundColor(getResources().getColor(R.color.appBarC));
        int accent = tinydb.getInt("colorAc");
        toolbar.setTitleTextColor(getResources().getColor(accent));
        editText2.setHintTextColor(getResources().getColor(accent));


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.appc));
            int color = getResources().getColor(R.color.appBarC);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, bm, color);

            setTaskDescription(td);
            bm.recycle();
        }

        toSendArray = tinydb.getListString(TabFragment1.currentChatName);
        Directions = tinydb.getListInt(TabFragment1.currentChatName + "d");
        Vstate = tinydb.getListInt(TabFragment1.currentChatName + "v");
        TaRR = tinydb.getListString(TabFragment1.currentChatName + "t");
        idforView = tinydb.getListString(TabFragment1.currentChatName + "i");
        forGroupM = tinydb.getListString(TabFragment1.currentChatName + "g");

        for (i = 0; i < toSendArray.size(); i++) {
            try{
                mAdapter2.addMessage(toSendArray.get(i), Directions.get(i), Vstate.get(i), TaRR.get(i), idforView.get(i), forGroupM.get(i));
            }catch (Exception b){}
        }

        if (mList2.getCount() > 7) {
            mList2.setStackFromBottom(true);
        }


        mList2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
                                           final int pos, long id) {
                final CharSequence[] items = {"Delete", "Resend","Copy"};

                AlertDialog.Builder builder = new AlertDialog.Builder(InChatCpn, R.style.YourAlertDialogTheme);
                builder.setTitle("What Do You Want To Do?");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        TextView tvrs = (TextView) arg1.findViewById(R.id.txtMessage);
                        String rsm = tvrs.getText().toString();

                        switch (item) {
                            case 0:
                                final CharSequence[] items2 = {"Yes", "No"};

                                AlertDialog.Builder builder2 = new AlertDialog.Builder(InChatCpn, R.style.YourAlertDialogTheme);
                                builder2.setTitle("Are You Sure?");
                                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item2) {
                                        switch (item2) {
                                            case 0:
                                                DeleteInChatM(pos);
                                                break;

                                            case 1:
                                                break;

                                            default:
                                        }

                                    }

                                });
                                AlertDialog alert2 = builder2.create();
                                alert2.show();


                                break;

                            case 1:

                                String tempidd = ChatCustomAdapter.MessageIdChat.get(pos);
                                SendTHMessage(editText2, tinydb, rsm, true, tempidd);

                                break;

                            case 2:
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("a", rsm);
                                clipboard.setPrimaryClip(clip);

                                Toast.makeText(MainActivity.cpn, "Message Was Copied", Toast.LENGTH_SHORT).show();

                                break;

                            default:

                        }

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }


        });
        send2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendTHMessage(editText2, tinydb, editText2.getText().toString(), false, "0");
            }
        });

        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(MainActivity.cpn, "Before", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(MainActivity.cpn, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

                //Toast.makeText(MainActivity.cpn, "After", Toast.LENGTH_SHORT).show();
            }
        });

        tinydb.putInt(TabFragment1.currentChatName+"u",0);
        if(TabFragment1.groupChat&&TabFragment1.mList!=null)
        {
            TabFragment1.mAdapter.notifyDataSetInvalidated();
        }
        else if(!TabFragment1.groupChat&&TabFragmment2.fList!=null)
        {
            TabFragmment2.fAdapter.notifyDataSetInvalidated();
        }

        if(Build.VERSION.SDK_INT>=23)
        {
            String reply = getMessageText(this.getIntent());
            System.out.println(reply);
            if (reply!=null&&editText2!=null)
            {
                NotificationManager notifManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notifManager.cancelAll();
                In_Chat.SendTHMessage(editText2, tinydb, reply, false, "0");
            }
        }

        if (MainActivity.mTcpClient!=null&& !TabFragment1.groupChat)
        {
            new User(this);
            String imin="209@"+TabFragment1.currentChatName+","+User.username+"*";
            MainActivity.mTcpClient.sendMessage(imin);

            new User(this);
            MainActivity.mTcpClient.sendMessage("1102@"+User.username+","+TabFragment1.currentChatName+"*");


        }


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        if (!MainActivity.active) {
            Intent a = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(a);
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        InChatActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        InChatActive = false;
        if (TabFragment1.groupChat) {
            TabFragment1.groupChat = false;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (Build.VERSION.SDK_INT>=23&&!MainActivity.active)
        {
            Intent myIntent = new Intent(MainActivity.cpn, MyBroadcastReciver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.cpn, 0, myIntent, 0);
            AlarmManager alarmManager = (AlarmManager)MainActivity.cpn.getSystemService(ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 1);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,Calendar.getInstance().getTimeInMillis()+1000,pendingIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        TinyDB tinydb = new TinyDB(this);
        boolean tg = tinydb.getBoolean(TabFragment1.currentChatName+"TG");//// TODO: 5/6/2016 still need to check it dosnt cause problems 
        if (tg)
        {
            getMenuInflater().inflate(R.menu.group_tg, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.menu_in__chat, menu);
        }

        MenuItem item = menu.findItem(R.id.Mute);
        getStateMute(this,item,false);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.Mute:
               getStateMute(this,item,true);
                break;
            case R.id.infoSet:
                Intent tta = new Intent(this,MembersSet.class);
                startActivity(tta);
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    public static void DeleteInChatM(int pos) {
        TinyDB tdb = new TinyDB(MainActivity.cpn);
        ChatCustomAdapter.AllChatData.remove(pos);
        ChatCustomAdapter.AllChatDirection.remove(pos);
        ChatCustomAdapter.MessageIdChat.remove(pos);
        ChatCustomAdapter.messages.remove(pos);
        ChatCustomAdapter.TimeArray.remove(pos);
        ChatCustomAdapter.VArray.remove(pos);

        tdb.putListString(TabFragment1.currentChatName, ChatCustomAdapter.AllChatData);
        tdb.putListInt(TabFragment1.currentChatName + "d", ChatCustomAdapter.AllChatDirection);
        tdb.putListString(TabFragment1.currentChatName + "i", ChatCustomAdapter.MessageIdChat);
        tdb.putListString(TabFragment1.currentChatName + "t", ChatCustomAdapter.TimeArray);
        tdb.putListInt(TabFragment1.currentChatName + "v", ChatCustomAdapter.VArray);

        mAdapter2.notifyDataSetChanged();
    }

    public static void SendTHMessage(EditText editText2, TinyDB tinydb, String mes, boolean resend, String idForResend) {
        MainActivity.incoming = false;
        int tttemp;
        int codeNum;

        String ends;
        String doneId;
        String id;
        String forGroupM;
        int tempint;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateandTime = sdf.format(new Date());


        if (!resend) {
            id = tinydb.getString(TabFragment1.currentChatName + "idCount");
        } else {
            id = idForResend;
        }


        if (resend) {
            tempint = Integer.valueOf(id);
            doneId = String.valueOf(tempint);
        } else {

            doneId = String.valueOf(ChatCustomAdapter.AllChatData.size());

        }

        toSendArray = new ArrayList<String>();
        toSendArray.add(TabFragment1.currentChatName);//the user to send user name
        toSendArray.add(tinydb.getString("TheUserName"));//the sender username
        toSendArray.add(doneId);
        toSendArray.add(currentDateandTime);
        toSendArray.add(mes);//the message


        if (TabFragment1.groupChat) {
            //toSendArray.add("g");
            boolean tg = tinydb.getBoolean(TabFragment1.currentChatName+"TG");
            if (tg)
            {
                codeNum = 211;
            }
            else
            {
                codeNum = 210;
            }
            //if its a group message
            new User(MainActivity.cpn);
            forGroupM = User.username;
        } else {
            //toSendArray.add("c");
            codeNum = 200;//if its a regular message
            forGroupM = "1";
        }


        ends = Communication.pattern(toSendArray, codeNum);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //add the text in the arrayList


        tttemp = 0;

        if (!resend) {

            In_Chat.mAdapter2.addMessage(mes, 1, tttemp, currentDateandTime, doneId, forGroupM);
            editText2.setText("");

            tinydb = new TinyDB(In_Chat.InChatCpn);
            tinydb.putListString(TabFragment1.currentChatName, ChatCustomAdapter.AllChatData);
            tinydb.putListInt(TabFragment1.currentChatName + "d", ChatCustomAdapter.AllChatDirection);
            tinydb.putListInt(TabFragment1.currentChatName + "v", ChatCustomAdapter.VArray);
            tinydb.putListString(TabFragment1.currentChatName + "t", ChatCustomAdapter.TimeArray);
            tinydb.putListString(TabFragment1.currentChatName + "i", ChatCustomAdapter.MessageIdChat);
            tinydb.putListString(TabFragment1.currentChatName + "g", ChatCustomAdapter.GroupMessUser);
        }


        //sends the message to the server
        if (MainActivity.mTcpClient != null) {

            MainActivity.mTcpClient.sendMessage(ends);
        }

        if(!TabFragment1.groupChat)
        {
            Communication.MoveChatToTop(TabFragment1.currentChatName, MainActivity.cpn, TabFragment1.groupChat);
        }


        if (MainActivity.active) {
            TabFragment1.mAdapter.notifyDataSetChanged();
            TabFragmment2.fAdapter.notifyDataSetChanged();
        }
    }

    public static void getStateMute(Context mc,MenuItem item,boolean change)
    {
        TinyDB tdb = new TinyDB(mc);
        Boolean type = TabFragment1.groupChat;
        String t;
        if (type){t="gg";}
        else {t="cc";}
        Boolean state = tdb.getBoolean(TabFragment1.currentChatName+t);

        Drawable d,d2;
        d = mc.getResources().getDrawable(R.drawable.ic_notifications_white_24dp);
        d2 = mc.getResources().getDrawable(R.drawable.ic_notifications_off_white_24dp);

        if (!state)
        {
            if (change){tdb.putBoolean(TabFragment1.currentChatName+t,true); item.setIcon(d2);}
            else {item.setIcon(d);}

        }
        else {

            if (change){tdb.putBoolean(TabFragment1.currentChatName+t,false);item.setIcon(d);}
            else {item.setIcon(d2);}
        }
        if (change)
        {
            if (t.equals("cc")&&TabFragmment2.fList != null) {TabFragmment2.fAdapter.notifyDataSetChanged();}
            else if (t.equals("gg")&&TabFragment1.mList!=null) {TabFragment1.mAdapter.notifyDataSetChanged();}
        }
    }
    public static boolean JustTheStateMute(Context mc)
    {
        TinyDB tdb = new TinyDB(mc);
        boolean type = TabFragment1.groupChat;
        String t;
        if (type){t="gg";}
        else {t="cc";}

        return tdb.getBoolean(TabFragment1.currentChatName+t);
    }
    public static Bitmap getDrawable(String name)
    {
        String search = name+"p.jpg";
        String Path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/LoCo/"+search;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            return BitmapFactory.decodeFile(Path, options);
        }catch (Exception aa){return null;}
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        try {
            Bitmap sbmp;
            if(bmp.getWidth() != radius || bmp.getHeight() != radius)
                sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
            else
                sbmp = bmp;
            Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                    sbmp.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.parseColor("#BAB399"));
            canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.1f,
                    sbmp.getWidth() / 2+0.1f, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(sbmp, rect, rect, paint);


            return output;
        }catch (Exception as){return null;}
    }

    public static String getMessageText(Intent intent) {
        if (Build.VERSION.SDK_INT >= 21) {
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            if (remoteInput != null) {
                return remoteInput.getString(Communication.KEY_TEXT_REPLY);
            }
            return null;
        }
        else
        {
            return null;
        }
    }

    public static void FinishChat()
    {
        if (ChatAc!=null)
        {
            try {
                ChatAc.finish();
            }catch (Exception a){}

        }
    }

    public static void changeToolBar(String mess)
    {
        if (toolbar!=null)
        {
            toolbar.setSubtitle(mess);
        }
    }


}
