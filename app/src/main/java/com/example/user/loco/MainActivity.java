package com.example.user.loco;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity {




    public static Context cpn;
    public static TCPClient mTcpClient;
    public static Boolean incoming;
    public static ArrayList<String> EmptyList;
    public static Activity maAc;

    public static ArrayList<String> tOSendLocation;
    public static String search ="";
    public static boolean active;
    public static boolean isLoged = false;
    public static MainActivity act;
    public static int TabPos;
    public static SearchView searchView;
    public static MenuItem searchMenu;
    public static boolean ddnzo=false;
    public static boolean onetimeting;
    public TinyDB tinydb;
    public static boolean ServerOnline;
    public static Toolbar toolbar;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);
        act = this;
        cpn = this;//the main activity context which is needed in other places
        PendingIntent pendingIntent;
        //setting up the variables for the tool bar and the tabs
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        ServerOnline = false;
        maAc=this;


        ViewPager pager;
        ViewPagerAdapter adapter;
        SlidingTabLayout tabs;
        CharSequence Titles[] = {"Groups", "Friends", "Requests"};
        int Numboftabs = 3;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.nav_frag);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);


        Intent a = new Intent(getApplicationContext(), Login.class);//when the user is not logged the app should show the log in screen


        tinydb = new TinyDB(this);
        isLoged = tinydb.getBoolean("isLoged");

        MyCustomAdapter.mListItems = tinydb.getListString("myUsers");//getting saved data
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        int accent =  tinydb.getInt("colorAc");
        final int AFinalColor = accent;

        toolbar.setBackgroundColor(getResources().getColor(R.color.appBarC));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        try {
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            upArrow.setColorFilter(getResources().getColor(accent), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }catch (Exception aac){}


        if (Build.VERSION.SDK_INT >= 21) {

            int color = getResources().getColor(R.color.appBarC);
            int color2 = getResources().getColor(R.color.appBarC);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, bm, color2);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));

            setTaskDescription(td);
            bm.recycle();
        }

        //Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (searchMenu!=null)
                {
                    searchMenu.collapseActionView();
                }
                posChange(position);
            }

            @Override
            public void onPageSelected(int position) {

                if (searchMenu!=null)
                {
                    searchMenu.collapseActionView();
                }
                posChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (TabFragment1.Fam!=null)
                {
                    TabFragment1.Fam.showMenuButton(true);
                }

            }
        });


        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width


        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(AFinalColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        tabs.setBackgroundColor(getResources().getColor(R.color.appBarC));

        // connect to the server through the service


        Intent myIntent = new Intent(this, MyBroadcastReciver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);

        if (Build.VERSION.SDK_INT>=23)
        {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,Calendar.getInstance().getTimeInMillis(),pendingIntent);
        }
        else
        {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    Calendar.getInstance().getTimeInMillis(), 1000*60, pendingIntent);
        }




        ArrayList arr = tinydb.getListString("myFriends");
        int size = arr.size();

        if (!isLoged) {
            tinydb.putBoolean("toggle",true);
            tinydb.putBoolean("vibe",true);
            startActivity(a);

        }

        boolean c2 = tinydb.getBoolean("c2");

        if (c2&&size!=0)
        {
            Intent jpp = new Intent(MainActivity.cpn.getApplicationContext(), PicRetrieve.class);
            MainActivity.cpn.startActivity(jpp);
            tinydb.putBoolean("c2",false);
        }




    }


    public void onBackPressed() {//when user is pressing back button the app wont go back to login screen
        // TODO Auto-generated method stub
        TinyDB tdb = new TinyDB(this);
        boolean state = tdb.getBoolean("drawerState");
        if (state)
        {
            NavigationDrawerFragment.mDrawerLayout.closeDrawer(NavigationDrawerFragment.v);
        }
        else if (TabFragment1.Fam!=null)
        {
            if (TabFragment1.Fam.isOpened()){TabFragment1.Fam.close(false);}
            else {moveTaskToBack(true);}

        }

    }

    //to know if the activity is running
    @Override
    public void onStart() {
        super.onStart();
        active = true;
        onetimeting=true;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        active = false;
        onetimeting=false;
        if (mTcpClient!=null)
        {
            new User(this);
            mTcpClient.sendMessage("1101@"+User.username+"*");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_test, menu);
        // Associate searchable configuration with the SearchView
        searchView = (SearchView) menu.findItem(R.id.search_action)
                .getActionView();

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                search = newText;
                if (!search.equals(""))
                {
                    switch (TabPos)
                    {
                        case 0:
                            TabFragment1.mAdapter.filterG(search);
                            break;

                        case 1:
                            TabFragmment2.fAdapter.filter(search);
                            break;

                        case 2:
                            TabFragment3.rAdapter.filterR(search);
                            break;
                        default:

                    }

                }
                else
                {

                    posEnd();
                }return true;
            }

            public boolean onQueryTextSubmit(String query) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                return true;
            }

        };
        searchView.setOnQueryTextListener(queryTextListener);
        MenuItem menuItem = menu.findItem(R.id.search_action);
        MenuItemCompat.setOnActionExpandListener(menuItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        // Return true to allow the action view to expand
                        tinydb.putBoolean("WeS",true);
                        ddnzo = true;
                        if (TabFragment1.Fam!=null)
                        {
                            TabFragment1.Fam.hideMenuButton(true);
                        }
                        return true;
                    }
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        // When the action view is collapsed, reset the query
                        tinydb.putBoolean("WeS",false);
                        if (TabFragment1.Fam!=null)
                        {
                            TabFragment1.Fam.showMenuButton(true);

                        }
                        if (ddnzo)
                        {
                            ddnzo=false;
                            TabFragment1.mAdapter.notifyDataSetInvalidated();
                        }
                        // Return true to allow the action view to collapse
                        return true;
                    }
                });


        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.addA) {
            Intent t = new Intent(getApplicationContext(), AddFriend.class);
            startActivity(t);
        }
        else if (id==R.id.search_action)
        {
            searchMenu=item;
        }

        return super.onOptionsItemSelected(item);
    }


    //all the code below this is for setting the tabs

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
            super(fm);

            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;

        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {

            if (position == 0) // if the position is 0 we are returning the First tab
            {
                return new TabFragment1.TabFragment12();
            } else if (position == 1)            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            {
                return new TabFragmment2.TabFragmment22();
            } else {

                return new TabFragment3.TabFragment33();
            }


        }

        // This method return the titles for the Tabs in the Tab Strip

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }

        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return NumbOfTabs;
        }
    }

    public static void posChange(int pos)
    {
        switch (pos)
        {
            case 0:
                TabPos=0;
                break;

            case 1:
                TabPos=1;
                break;

            case 2:
                TabPos=2;
                break;
            default:
        }
    }
    public static void posEnd()
    {
        TinyDB tdb = new TinyDB(MainActivity.cpn);
        switch (TabPos)
        {
            case 0:
                MyCustomAdapter.mListItems=tdb.getListString("myUsers");
                TabFragment1.mAdapter.notifyDataSetChanged();
                break;

            case 1:
                FriendCustomAdapter.FrListItems = tdb.getListString("myFriends");
                TabFragmment2.fAdapter.notifyDataSetChanged();
                break;

            case 2:
                RequestCostumAdapter.rListItems = tdb.getListString("myRequests");
                TabFragment3.rAdapter.notifyDataSetChanged();
                break;
            default:

        }
    }

    public static void ActionBarServer(String sstat)
    {
        if (toolbar!=null&&cpn!=null)
        {
            final String stat = sstat;
            if (stat.equals("1"))
            {
                maAc.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toolbar.setTitleTextColor(cpn.getResources().getColor(R.color.okGreen));
                    }
                });
            }

            else if (stat.equals("0"))
            {
                maAc.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toolbar.setTitleTextColor(cpn.getResources().getColor(R.color.badRed));
                        ServerOnline=false;
                    }
                });

            }

        }
    }


}