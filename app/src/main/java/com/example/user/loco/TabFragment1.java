package com.example.user.loco;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

// this is the main activity layout fragment code this was created for the tabs
public class TabFragment1 extends Activity {

    public static ListView mList;
    public static MyCustomAdapter mAdapter;
    public static String currentChatName;
    public static boolean groupChat = false;
    public static FloatingActionMenu Fam;



    public static class TabFragment12 extends Fragment {

        View rootView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.activity_tab_fragment1, container, false);
            mList = (ListView) rootView.findViewById(R.id.list);

            final TinyDB tinydb = new TinyDB(MainActivity.cpn);
            MyCustomAdapter.mListItems = tinydb.getListString("myUsers");
            MyCustomAdapter.SearchG = tinydb.getListString("myUsers");
            int accent =  tinydb.getInt("colorAc");
            Fam = (FloatingActionMenu)rootView.findViewById(R.id.fabb);

            com.github.clans.fab.FloatingActionButton fabb1= (com.github.clans.fab.FloatingActionButton )rootView.findViewById(R.id.fab1);
            com.github.clans.fab.FloatingActionButton fabb2= (com.github.clans.fab.FloatingActionButton )rootView.findViewById(R.id.fab2);

            fabb1.setColorNormal(getResources().getColor(accent));
            fabb2.setColorNormal(getResources().getColor(accent));
            Fam.setMenuButtonColorPressed(getResources().getColor(accent));

            if (Build.VERSION.SDK_INT>=21)
            {
                Fam.setElevation((float)200);
            }

            fabb1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent d = new Intent(rootView.getContext(), CreateGroup.class);
                    startActivity(d);
                }
            });
            fabb2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent a = new Intent(rootView.getContext(), JoinGroup.class);
                    startActivity(a);
                }
            });

            mAdapter = new MyCustomAdapter(MainActivity.cpn, MyCustomAdapter.mListItems);//sending the saved list of chats(users)

            TextView emptyText = (TextView) rootView.findViewById(android.R.id.empty);

            mList.setEmptyView(emptyText);
            mList.setAdapter(mAdapter);

            mList.setOnScrollListener(new AbsListView.OnScrollListener() {
                int last_item;
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    boolean wes = tinydb.getBoolean("WeS");
                    if(last_item<firstVisibleItem+visibleItemCount-1&&firstVisibleItem!=0){
                        Fam.hideMenuButton(true);

                    }
                    else if(last_item>firstVisibleItem+visibleItemCount-1&&!wes){
                        Fam.showMenuButton(true);

                    }
                    last_item = firstVisibleItem+visibleItemCount-1;
                }
            });


            // when user on a list item
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    groupChat = true;
                    Intent f = new Intent(MainActivity.cpn, In_Chat.class);
                    startActivity(f);

                    TextView textView = (TextView) view.findViewById(R.id.list_item_text_view);
                    currentChatName = textView.getText().toString();

                }
            });
            mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view1, int i, long l) {
                    final View vv = view1;
                    int sepP = tinydb.getInt("TheSep");
                    if (i>=sepP)
                    {
                        final CharSequence[] items = {"Yes", "No"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.cpn, R.style.YourAlertDialogTheme);
                        builder.setTitle("Do You Want To Delete This Group?");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {

                                switch (item) {

                                    case 0:
                                        new User(getActivity());
                                        String un=User.username;
                                        TextView textView = (TextView) vv.findViewById(R.id.list_item_text_view);
                                        String name = textView.getText().toString();

                                        ArrayList jts = new ArrayList<String>();
                                        jts.add(un);
                                        jts.add(name);

                                        String donzo = Communication.pattern(jts,902);

                                        if (MainActivity.mTcpClient != null) {

                                            MainActivity.mTcpClient.sendMessage(donzo);
                                        }
                                        break;

                                    default:

                                }

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        return true;

                    }
                    else
                    {
                        Toast.makeText(getActivity(), "You Cant Delete This Type Of Groups", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            });

            return rootView;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mList.setOnScrollListener(null);
        }

    }


}


