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
import java.util.concurrent.ExecutionException;


public class FriendCustomAdapter extends BaseAdapter {

    public static ArrayList<String> FrListItems;
    public static ArrayList<Pair<String,View>> picsFr;
    public static ArrayList<String> SearchFriends;
    private LayoutInflater mLayoutInflater;

    public FriendCustomAdapter(Context context,ArrayList<String> arrayDone) {


        //get the layout inflater
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrListItems = arrayDone;
        picsFr = new ArrayList<Pair<String, View>>();

    }

    public void addUser(String user) {

        FrListItems.add(user);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return FrListItems.size();
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
        view=null;

        view = mLayoutInflater.inflate(R.layout.friend_item, null);


        //get the string item from the position "position" from array list to put it on the TextView
        int i;
        TinyDB tdb = new TinyDB(MainActivity.cpn);
        String stringItem = FrListItems.get(position);
        String lastm=null;
        String lastTime=null;
        String lasmShort="";
        String lmReg="";
        if(tdb.getListString(stringItem).size()!=0)
        {
            try
            {
                lastm = tdb.getListString(stringItem).get(tdb.getListString(stringItem).size() - 1);
                lastTime = tdb.getListString(stringItem+"t").get(tdb.getListString(stringItem).size() - 1);
            }catch (Exception a){}

        }
        else
        {
            lastm = null;
            lastTime=null;
        }



        if (stringItem != null) {
            TextView itemName = (TextView) view.findViewById(R.id.friend_item_text_view);

            if (itemName != null) {
                //set the item name on the TextView
                itemName.setText(stringItem);

            }
        }
        TinyDB tinydb = new TinyDB(MainActivity.cpn);
        if (lastm!=null)
        {
            TextView lmtext = (TextView) view.findViewById(R.id.lastMess);

            lmtext.setTextColor(MainActivity.cpn.getResources().getColor(R.color.appBarC));
            int size = lastm.length();
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

        int accent =  tinydb.getInt("colorAc");
        TextView lmTimeText = (TextView) view.findViewById(R.id.lastMessTime);
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
        picsFr.add(new Pair<String, View>(stringItem,view));
        Communication.JustPutIt(view,stringItem+"p");//just for test need to be the name
        //this method must return the view corresponding to the data at the specified position.
        boolean mute = tdb.getBoolean(stringItem+"cc");
        if (mute){
            ImageView img = (ImageView)view.findViewById(R.id.muteIV);
            Drawable d = view.getResources().getDrawable(R.drawable.ic_notifications_off_black_18dp);
            img.setImageDrawable(d);
        }
        return view;
    }

    public void filter(String charText)
    {
        FrListItems.clear();


        for (String wp : SearchFriends)
        {
            if (wp.toLowerCase().contains(charText.toLowerCase()))
            {
                FrListItems.add(wp);
            }
        }

        notifyDataSetChanged();
    }
}

