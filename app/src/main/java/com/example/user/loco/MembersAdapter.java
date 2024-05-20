package com.example.user.loco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MembersAdapter extends BaseAdapter {

    public static ArrayList<String> membersArr;//
    private LayoutInflater mLayoutInflater;


    public MembersAdapter(Context context,ArrayList<String> test) {

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        membersArr =test;

    }

    public void addMessage(String m) {//adding a message to the list

        membersArr.add(m);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return membersArr.size();
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

        if (view == null) {
            int res = R.layout.member_list;

            view = mLayoutInflater.inflate(res, viewGroup, false);

        }


        String stringItem = membersArr.get(position);
        if (stringItem != null) {

            TextView itemName = (TextView) view.findViewById(R.id.MemName);

            if (itemName != null) {
                //set the item name on the TextView
                itemName.setText(stringItem);
            }
        }

        return view;

    }
}
