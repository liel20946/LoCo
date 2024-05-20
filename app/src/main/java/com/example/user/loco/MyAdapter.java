package com.example.user.loco;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class MyAdapter extends BaseAdapter {


    public static ArrayList<Integer> icon;//
    public static ArrayList<String> Text;//
    private LayoutInflater mLayoutInflater;


    public MyAdapter(Context context) {

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        icon = new ArrayList<Integer>();
        Text = new ArrayList<String>();

    }

    public void addMessage(int message, String m) {//adding a message to the list

        icon.add(message);
        Text.add(m);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return icon.size();
    }

    @Override
    //get the data of an item from a specific position
    //i represents the position of the item in the list
    public Object getItem(int i) {
        return icon.get(i);
    }

    @Override
    public int getViewTypeCount() {
        return icon.size();
    }

    @Override
    //get the position id of the item from the list
    public long getItemId(int i) {
        return i;
    }

    public int getItemViewType(int i) {
        return icon.get(i);
    }

    @Override

    public View getView(int position, View view, ViewGroup viewGroup) {

        int direction = getItemViewType(position);

        //check to see if the reused view is null or not, if is not null then reuse it
        view = null;

        if (view == null) {
            int res;


            if (direction == 0) {

                res = R.layout.item_row;

            }
            else if (direction==1)
            {
                res = R.layout.item_row_set;
            }

            else if (direction == 2) {

                res = R.layout.item_row_loc;
            }

            else if (direction == 3) {

                res = R.layout.item_row_info;
            }
            else
            {
                res = R.layout.item_row_rest;
            }


            view = mLayoutInflater.inflate(res, viewGroup, false);

        }


        String stringItem = Text.get(position);
        if (stringItem != null) {

            TextView itemName = (TextView) view.findViewById(R.id.rowText);

            if (itemName != null) {
                //set the item name on the TextView
                itemName.setText(stringItem);
                itemName.setTextSize((float)14);
            }
        }

        return view;

    }
}


