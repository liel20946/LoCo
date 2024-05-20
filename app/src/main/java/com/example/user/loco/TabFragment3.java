package com.example.user.loco;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


//this is the requests fragment code this was created for the tabs
public class TabFragment3 extends Activity {

    public static ListView rList;
    public static RequestCostumAdapter rAdapter;//needs requst adapter not custom
    public static String currentRequestName;


    public static class TabFragment33 extends Fragment {

        View rootView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.activity_tab_fragment3, container, false);
            rList = (ListView) rootView.findViewById(R.id.RequestListV);

            TinyDB tinydb = new TinyDB(MainActivity.cpn);
            RequestCostumAdapter.rListItems = tinydb.getListString("myRequests");
            RequestCostumAdapter.SearchR = tinydb.getListString("myRequests");

            rAdapter = new RequestCostumAdapter(MainActivity.cpn);//sending the saved list of chats(users)
            TextView emptyText = (TextView) rootView.findViewById(android.R.id.empty);
            rList.setEmptyView(emptyText);
            rList.setAdapter(rAdapter);


            // when user on a list item
            rList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    TextView textView = (TextView) view.findViewById(R.id.request_item_text_view);
                    currentRequestName = textView.getText().toString();


                }
            });


            return rootView;

        }


    }


}


