package com.example.user.loco;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;




public class NavigationDrawerFragment extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;
    public static DrawerLayout mDrawerLayout;
    public static View v;

    public static ListView tList;
    public static MyAdapter tAdapter;
    public static Context NavigationCpn;
    public  static CircleImageView iv;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        final TinyDB tinydb = new TinyDB(v.getContext());
        NavigationCpn = getActivity();
        iv = (CircleImageView)v.findViewById(R.id.profile_image);
        setImageDrawer(iv,tinydb,getActivity());

        tList = (ListView) v.findViewById(R.id.fragDrawerList);
        tAdapter = new MyAdapter(getActivity());//sending the saved list of chats(users)
        tAdapter.addMessage(3,"  Personal Information");
        tAdapter.addMessage(2,"  Send Location");
        tAdapter.addMessage(1,"  Settings");
        tAdapter.addMessage(4,"  Restore Data");
        tAdapter.addMessage(0,"  Log Out");

        tList.setAdapter(tAdapter);


        tList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                new User(getActivity());
                switch (position){
                    case 0:
                        mDrawerLayout.closeDrawer(v);
                        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                            @Override
                            public void run() {
                                // This method will be executed once the timer is over
                                // Start your app main activity
                                EditInformation();
                            }
                        }, 350);
                        break;

                    case 1:
                        mDrawerLayout.closeDrawer(v);
                        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                            @Override
                            public void run() {
                                // This method will be executed once the timer is over
                                // Start your app main activity
                                sendLocation();
                            }
                        }, 350);

                        break;

                    case 2:
                        mDrawerLayout.closeDrawer(v);
                        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                            @Override
                            public void run() {
                                // This method will be executed once the timer is over
                                // Start your app main activity
                                Intent l = new Intent(getActivity(), Settings.class);
                                startActivity(l);
                            }
                        }, 350);
                        break;

                    case 3:
                        ArrayList arr = tinydb.getListString("myFriends");
                        int size = arr.size();
                        if (size!=0)
                        {
                            tinydb.putInt("prog",0);
                            new User(MainActivity.cpn);
                            String ddends = "505@"+User.username+"*";
                            if (MainActivity.mTcpClient != null) {
                                MainActivity.mTcpClient.sendMessage(ddends);
                            }
                            Intent jpp = new Intent(MainActivity.cpn, PicRetrieve.class);
                            startActivity(jpp);
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "You Have No Data To Restore", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                        LogOut();
                }

            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Intent i = new Intent(Intent.ACTION_PICK_ACTIVITY, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               // startActivityForResult(i,SELECTED_PICTURE);

                Intent a = new Intent(getActivity(),CropPic.class);
                startActivity(a);


            }
        });



        int accent;


        accent = tinydb.getInt("colorAc");
        if (accent==0||accent==2131427329)
        {
            accent = R.color.LoCoOrange;
            tinydb.putInt("colorAc",accent);
        }
             // Inflate the layout for this fragment
        return v;
    }




    public void setUp(DrawerLayout drawerLayout, Toolbar toolbar) {

        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                TinyDB tdb = new TinyDB(getActivity());
                tdb.putBoolean("drawerState",true);

                setImageDrawer(iv,tdb,getActivity());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                TinyDB tdb = new TinyDB(getActivity());
                tdb.putBoolean("drawerState",false);
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();

            }
        });
    }

    public  void LogOut() {


        MainActivity.EmptyList = new ArrayList<String>();
        new User(MainActivity.cpn);

        MainActivity.EmptyList.add(User.username);

        if (MainActivity.mTcpClient != null) {

            MainActivity.mTcpClient.sendMessage(Communication.pattern(MainActivity.EmptyList, 101));
        }

    }

    //sending the location
    public void sendLocation() {
        new User(getActivity());
        final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        else
        {
            Location.getLocation(getActivity());
            String location;
            String adress;

            location = Location.Convert(getActivity());
            MainActivity.tOSendLocation = new ArrayList<String>();

            MainActivity.tOSendLocation.add(User.username);

            MainActivity.tOSendLocation.add(String.valueOf(location));


            adress = Communication.pattern(MainActivity.tOSendLocation, 300);


            if (MainActivity.mTcpClient != null) {

                MainActivity.mTcpClient.sendMessage(adress);
            }

            MainActivity.tOSendLocation.clear();



        }

    }



    public void EditInformation()
    {
        Intent a = new Intent(getActivity().getApplicationContext(),EditInfo.class);
        startActivity(a);
    }

    private void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.YourAlertDialogTheme);
        builder.setMessage("Your GPS Is disabled, do you want to turn it on?")
                .setCancelable(true)
                .setPositiveButton("Yes Sure", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Not Right now", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static void setImageDrawer(ImageView iv,TinyDB tinydb,Context c)
    {
        String uriS = tinydb.getString("uriS");
        if (!uriS.equals(""))
        {
            File imgFile = new  File(uriS);
            if(imgFile.exists()&&iv!=null){


                Glide.with(c)
                        .load(imgFile)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(iv);
            }
        }

    }

}
