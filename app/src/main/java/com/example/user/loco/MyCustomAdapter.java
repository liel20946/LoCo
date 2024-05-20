package com.example.user.loco;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MyCustomAdapter extends BaseAdapter {

    public static ArrayList<String> mListItems;
    public static ArrayList <Pair<String,View>> PicsHelp;
    public static ArrayList<String> SearchG;
    private LayoutInflater mLayoutInflater;

    public MyCustomAdapter(Context context, ArrayList<String> arrayDone) {


        //get the layout inflater
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListItems = arrayDone;
        PicsHelp = new ArrayList<Pair<String,View>>();
    }

    public void addUser(String user) {//adding a group to the list

        mListItems.add(user);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return mListItems.size();
    }

    @Override
    //get the data of an item from a specific position
    //i represents the position of the item in the list
    public Object getItem(int i) {
        return null;
    }


    @Override
    //get the position id of the item from the list
    public long getItemId(int i) {
        return 0;
    }

    @Override

    public View getView(int position, View view, ViewGroup viewGroup) {

        //check to see if the reused view is null or not, if is not null then reuse it
        view = null;

        view = mLayoutInflater.inflate(R.layout.list_item, null);

        int i;
        TinyDB tdb = new TinyDB(MainActivity.cpn);
        String lastm;
        String lastTime;
        String lasmShort="";
        String lmReg="";

        //get the string item from the position "position" from array list to put it on the TextView
        String stringItem = mListItems.get(position);


        if(tdb.getListString(stringItem).size()!=0)
        {
            lastm = tdb.getListString(stringItem).get(tdb.getListString(stringItem).size() - 1);
            lastTime = tdb.getListString(stringItem+"t").get(tdb.getListString(stringItem).size() - 1);
        }
        else
        {
            lastm = null;
            lastTime=null;
        }

        TextView itemName = (TextView) view.findViewById(R.id.list_item_text_view);
        if (stringItem != null) {


            if (itemName != null) {
                //set the item name on the TextView
                itemName.setText(stringItem);
            }
        }

        if (lastm!=null)
        {
            TextView lmtext = (TextView) view.findViewById(R.id.gLastM);
            int size = lastm.length();

            lmtext.setTextColor(MainActivity.cpn.getResources().getColor(R.color.appBarC));

            if (size<49)
            {
                for(i=0;i<size;i++)
                {
                    if (lastm.charAt(i)=='\n')
                    {
                        lmReg+=' ';
                    }
                    else
                    {
                        lmReg+= lastm.charAt(i);
                    }

                }
                lmtext.setText(lmReg);
            }
            else
            {
                for(i=0;i<46;i++)
                {

                    if (lastm.charAt(i)=='\n')
                    {
                        lasmShort+=' ';
                    }
                    else
                    {
                        lasmShort+= lastm.charAt(i);
                    }

                }

                lasmShort+="...";
                lmtext.setText(lasmShort);
            }
        }
        int accent =  tdb.getInt("colorAc");
        TextView lmTimeText = (TextView) view.findViewById(R.id.gLastT);
        lmTimeText.setTextColor(MainActivity.cpn.getResources().getColor(accent));
        if(lastTime!=null)
        {
            lmTimeText.setText(lastTime);
        }

        int unr = tdb.getInt(stringItem+"u");
        TextView unRead = (TextView) view.findViewById(R.id.UnReadNum);
        if (unr!=0)
        {

            String forCon = String.valueOf(unr);
            if (unr>9)
            {
                unRead.setText(" "+forCon);
            }
            else
            {
                unRead.setText("  "+forCon);
            }

        }
        else
        {
            unRead.setText("");
            unRead.setBackgroundColor(view.getResources().getColor(R.color.BaseColor));
        }
        //PicsHelp.add(new Pair<String, View>(stringItem,view));
        //Communication.JustPutIt(view,stringItem+"p");  //just for test need to be the name
        boolean mute = tdb.getBoolean(stringItem+"gg");
        if (mute){
            ImageView img = (ImageView)view.findViewById(R.id.MuteIV);
            Drawable d = view.getResources().getDrawable(R.drawable.ic_notifications_off_black_18dp);
            img.setImageDrawable(d);
        }
        //need to check
        int sep = tdb.getInt("TheSep");//need to check
        boolean wes = tdb.getBoolean("WeS");
        TextView tempL = (TextView) view.findViewById(R.id.TempL);
        if (position==0&&!wes)
        {
            tempL.setText("Local Groups");
            //tempL.setBackgroundColor(MainActivity.cpn.getResources().getColor(accent));
            tempL.setVisibility(View.VISIBLE);

        }
        else if (position==sep&&!wes)
        {
           tempL.setText("Global Groups");
           // tempL.setBackgroundColor(MainActivity.cpn.getResources().getColor(accent));
            tempL.setVisibility(View.VISIBLE);
        }


        //this method must return the view corresponding to the data at the specified position.
        return view;
    }

    public void filterG(String charText)
    {
        mListItems.clear();


        for (String wp : SearchG)
        {
            if (wp.toLowerCase().contains(charText.toLowerCase()))
            {
                mListItems.add(wp);
            }
        }

        notifyDataSetChanged();
    }
}