package com.example.user.loco;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class TabFragmment2 extends Activity//this is the friends activity this is for tabs
{

    public static ListView fList;
    public static FriendCustomAdapter fAdapter;


    public static class TabFragmment22 extends Fragment {
        View rootView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.activity_tab_fragmment2, container, false);
            fList = (ListView) rootView.findViewById(R.id.friendListV);
            TinyDB tinydb = new TinyDB(MainActivity.cpn);
            FriendCustomAdapter.FrListItems = tinydb.getListString("myFriends");
            FriendCustomAdapter.SearchFriends = tinydb.getListString("myFriends");
            fAdapter = new FriendCustomAdapter(MainActivity.cpn, FriendCustomAdapter.FrListItems);//sending the saved list of chats(users)

            TextView emptyText = (TextView) rootView.findViewById(android.R.id.empty);
            fList.setEmptyView(emptyText);
            fList.setAdapter(fAdapter);

            // when user on a list item
            fList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent f = new Intent(MainActivity.cpn, In_Chat.class);
                    startActivity(f);

                    TextView textView = (TextView) view.findViewById(R.id.friend_item_text_view);
                    TabFragment1.currentChatName = textView.getText().toString();

                }
            });

            //when user long press on a list item
            fList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub

                    TextView textView = (TextView) arg1.findViewById(R.id.friend_item_text_view);
                    TabFragment1.currentChatName = textView.getText().toString();


                    //starting a dialog for the user
                    final CharSequence[] items = {"Quick Info", "Delete Friend", "Report User"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.cpn, R.style.YourAlertDialogTheme);
                    builder.setTitle("What Do You Want To Do?");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            switch (item) {

                                case 0:
                                    User.getInfo();

                                    break;

                                case 1:
                                    User.deleteUser();
                                    break;

                                case 2:
                                    User.reportUser();

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
            return rootView;
        }

    }

}

