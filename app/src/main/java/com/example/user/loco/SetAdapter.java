package com.example.user.loco;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.ArrayList;


public class SetAdapter extends BaseAdapter {


    public static ArrayList<Integer> iconS;//
    public static ArrayList<String> TextS;//
    private LayoutInflater mLayoutInflater;


    public SetAdapter(Context context) {

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        iconS = new ArrayList<Integer>();
        TextS = new ArrayList<String>();

    }

    public void addMessage(int message, String m) {//adding a message to the list

        iconS.add(message);
        TextS.add(m);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return iconS.size();
    }

    @Override
    //get the data of an item from a specific position
    //i represents the position of the item in the list
    public Object getItem(int i) {
        return iconS.get(i);
    }

    @Override
    public int getViewTypeCount() {
        return iconS.size();
    }

    @Override
    //get the position id of the item from the list
    public long getItemId(int i) {
        return i;
    }

    public int getItemViewType(int i) {
        return iconS.get(i);
    }

    @Override

    public View getView(int position, View view, ViewGroup viewGroup) {

        int direction = getItemViewType(position);

        //check to see if the reused view is null or not, if is not null then reuse it
        view = null;

        if (view == null) {
            int res;


            if (direction == 0) {

                res = R.layout.set_not;

            }
            else if (direction == 1) {

                res = R.layout.set_vibe;
            }
            else if (direction==2)
            {
                res = R.layout.set_col;
            }
            else
            {
                res = R.layout.set_del;
            }


            view = mLayoutInflater.inflate(res, viewGroup, false);

        }


        String stringItem = TextS.get(position);
        if (stringItem != null) {

            TextView itemName = (TextView) view.findViewById(R.id.rowText);

            if (itemName != null) {
                //set the item name on the TextView
                itemName.setText(stringItem);
                itemName.setTextSize((float)14);
            }
        }

        final TinyDB tdb = new TinyDB(view.getContext());
        boolean state = tdb.getBoolean("toggle");
        boolean state2 = tdb.getBoolean("vibe");

        if (direction==0)
        {
            final CheckBox tbtn = (CheckBox) view.findViewById(R.id.notCB);


            if (state) {
                tbtn.setChecked(true);
            } else {
                tbtn.setChecked(false);
            }



            tbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        tdb.putBoolean("toggle",true);
                        //buttonView.setBackgroundColor(getResources().getColor(R.color.appBarC));
                        //tbtn.setText(" ON  ");
                    } else {
                        tdb.putBoolean("toggle", false);
                        //buttonView.setBackgroundColor(getResources().getColor(R.color.LoCoOrange));
                        //tbtn.setText(" OFF  ");
                    }
                }
            });



        }

        else if (direction==1)
        {
            final CheckBox vibration = (CheckBox) view.findViewById(R.id.vibeCB);

            vibration.setChecked(state);
            if (state2) {
                vibration.setChecked(true);
            } else {
                vibration.setChecked(false);
            }

            vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        tdb.putBoolean("vibe",true);
                        //buttonView.setBackgroundColor(getResources().getColor(R.color.appBarC));
                        //tbtn.setText(" ON  ");
                    } else {
                        tdb.putBoolean("vibe", false);
                        //buttonView.setBackgroundColor(getResources().getColor(R.color.LoCoOrange));
                        //tbtn.setText(" OFF  ");
                    }
                }
            });
        }

        return view;

    }
}


