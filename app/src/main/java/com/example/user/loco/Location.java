package com.example.user.loco;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;


import java.util.List;
import java.util.Locale;



public class Location {



    public static double lat;
    public static double lon;



    public static void getLocation(Context context)//getting the locatuon by the gps
    {




        GPSTracker gps;
        gps = new GPSTracker(context);

        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            lon = gps.getLongitude();

        }

        gps.stopUsingGPS();

    }

    public static String Convert(final Context context)//coverting the location to an adress
    {

        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null) {
                strAdd = addresses.get(0).getLocality();

            } else {

            }
        } catch (Exception e) {

        }
        return strAdd;

    }



}
