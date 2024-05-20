package com.example.user.loco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class RequestCostumAdapter extends BaseAdapter {

    private ArrayList<String> ListForReq;
    public static ArrayList<String> rListItems;
    private LayoutInflater rLayoutInflater;
    public static ArrayList <String> SearchR;

    public RequestCostumAdapter(Context context) {


        //get the layout inflater
        rLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // rListItems = arrayDone;
    }

    public void addUser(String user) {

        rListItems.add(user);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return rListItems.size();
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
        view = rLayoutInflater.inflate(R.layout.request_item, null);


        //get the string item from the position "position" from array list to put it on the TextView
        final String stringItem = rListItems.get(position);
        if (stringItem != null) {
            TextView itemName = (TextView) view.findViewById(R.id.request_item_text_view);

            if (itemName != null) {
                //set the item name on the TextView
                itemName.setText(stringItem);
            }
        }

        //// here are the listeners for the request denied or accepted


        ImageButton imgButton = (ImageButton) view.findViewById(R.id.reqAcpt);
        ImageButton imgButton2 = (ImageButton) view.findViewById(R.id.reqDeny);

        imgButton.setTag(position);
        imgButton2.setTag(position);



        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TinyDB tinydb = new TinyDB(MainActivity.cpn);


                if(Communication.DoesFriendEx(stringItem))
                {
                    rListItems.remove(stringItem);
                    TabFragment3.rAdapter.notifyDataSetChanged();
                    tinydb.putListString("myRequests", RequestCostumAdapter.rListItems);
                    Toast.makeText(view.getContext(), stringItem+" Is Already Your Friend", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    ListForReq = new ArrayList<String>();
                    ListForReq.add(stringItem);
                    ListForReq.add(User.username);

                    String ends = Communication.pattern(ListForReq, 602);



                    //sends the message to the server
                    if (MainActivity.mTcpClient != null) {

                        MainActivity.mTcpClient.sendMessage(ends);
                    }
                }


            }
        });

        imgButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),stringItem+" Was Denied ", Toast.LENGTH_SHORT).show();

                TinyDB tinydb = new TinyDB(MainActivity.cpn);

                rListItems.remove(stringItem);
                TabFragment3.rAdapter.notifyDataSetChanged();
                tinydb.putListString("myRequests", RequestCostumAdapter.rListItems);

                ListForReq = new ArrayList<String>();
                ListForReq.add(stringItem);
                ListForReq.add(User.username);

                String ends = Communication.pattern(ListForReq, 603);



                //sends the message to the server
                if (MainActivity.mTcpClient != null) {

                    MainActivity.mTcpClient.sendMessage(ends);
                }

            }
        });
        //this method must return the view corresponding to the data at the specified position.
        return view;
    }

    public void filterR(String charText)
    {
        rListItems.clear();


        for (String wp : SearchR)
        {
            if (wp.toLowerCase().contains(charText.toLowerCase()))
            {
                rListItems.add(wp);
            }
        }

        notifyDataSetChanged();
    }
}
