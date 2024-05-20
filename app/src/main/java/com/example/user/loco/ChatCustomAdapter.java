package com.example.user.loco;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ChatCustomAdapter extends BaseAdapter {


    public static List<Pair<String, Integer>> messages;//this aray contain the messages and color of the messages
    public static ArrayList<String> AllChatData;//this is for saving the data
    public static ArrayList<Integer> AllChatDirection;//also for saving the data
    public static ArrayList<Integer> VArray;
    public static ArrayList<String> TimeArray;
    public static ArrayList<String> MessageIdChat;
    public static ArrayList<String> GroupMessUser;
    private LayoutInflater mLayoutInflater;

    public ChatCustomAdapter(Context context) {


        //get the layout inflater
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        messages = new ArrayList<Pair<String, Integer>>();
        AllChatData = new ArrayList<String>();
        AllChatDirection = new ArrayList<Integer>();
        VArray= new ArrayList<Integer>();
        MessageIdChat =  new ArrayList<String>();
        TimeArray = new ArrayList<String>();
        GroupMessUser = new ArrayList<String>();
    }

    public void addMessage(String message, int direction,int v,String time,String id,String groupmess) {//adding a message to the list

        messages.add(new Pair(message, direction));
        AllChatData.add(message);
        AllChatDirection.add(direction);
        VArray.add(v);
        TimeArray.add(time);
        MessageIdChat.add(id);
        GroupMessUser.add(groupmess);
        try
        {
            notifyDataSetChanged();
        }catch (Exception a){}

    }

    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return messages.size();
    }

    @Override
    //get the data of an item from a specific position
    //i represents the position of the item in the list
    public Object getItem(int i) {return messages.get(i);}

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    //get the position id of the item from the list
    public long getItemId(int i) {
        return i;
    }

    public int getItemViewType(int i) {
        return messages.get(i).second;
    }

    @Override

    public View getView(int position, View view, ViewGroup viewGroup) {

        int direction = getItemViewType(position);

        //check to see if the reused view is null or not, if is not null then reuse it
        view = null;
        boolean Mtype = TabFragment1.groupChat;
        int varrpos=0;

        try {
            varrpos = VArray.get(position);
        }catch (Exception a){}

        if (view == null) {
            int res = 0;


            if (direction == 0) {
                if (Mtype)
                {
                    res = R.layout.groupchat_left;
                }
                else
                {
                    res = R.layout.message_left;
                }

            }
            else if (direction == 1) {
                if(varrpos==0)
                {
                    if (Mtype)
                    {
                        res =R.layout.groupchat1_item;
                    }
                    else
                    {
                        res = R.layout.inchat_item;
                    }

                }
                else if (varrpos==1)
                {
                    if (Mtype)
                    {
                        res =R.layout.groupchat2_item;
                    }
                    else {

                        res = R.layout.inchat1_item;

                    }
                }
                else if(varrpos==2)
                {
                    if (Mtype)
                    {
                        res =R.layout.groupchat3_item;
                    }
                    else {
                        res = R.layout.inchat2_item;

                    }

                }
                else
                {
                    if (!Mtype)
                    {
                        res = R.layout.inchat3_item;
                    }
                    else
                    {
                        res =R.layout.groupchat3_item;//just for now dost support it yet so it dosnt matter
                    }
                }
            }
            view = mLayoutInflater.inflate(res, viewGroup, false);

        }
        String stringItem=null;
        String UserNameItem=null;
        String hour = null;
        try {
            stringItem = messages.get(position).first;
            UserNameItem = GroupMessUser.get(position);
            hour = TimeArray.get(position);
        }catch (Exception a){}


        //get the string item from the position "position" from array list to put it on the TextView

        if (stringItem != null) {

            TextView itemName = (TextView) view.findViewById(R.id.txtMessage);

            if (itemName != null) {
                //set the item name on the TextView
                itemName.setText(stringItem);
            }

            if (Mtype&&direction==0)
            {
                TextView GUName = (TextView)view.findViewById(R.id.GroupMName);
                GUName.setText(UserNameItem+":");
            }
        }

        if(hour!=null)
        {
            TextView hourtext = (TextView) view.findViewById(R.id.timeD);
            hourtext.setText(hour);
        }

        //this method must return the view corresponding to the data at the specified position.
        return view;

    }


}