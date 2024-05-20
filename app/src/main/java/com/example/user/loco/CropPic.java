package com.example.user.loco;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.soundcloud.android.crop.Crop;

import java.io.File;



public class CropPic extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_croppic);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT>=21)
        {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.appBarC));
        }
        Crop.pickImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {

        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/LoCo";
        File dir = new File(file_path);
        File donzo = new File(dir,"proPic"+".jpg");
        Uri uri2= Uri.fromFile(donzo);
        Crop.of(source, uri2).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK&&NavigationDrawerFragment.iv!=null) {
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/LoCo";
            File dir = new File(file_path);
            File donzo = new File(dir,"proPic"+".jpg");
            TinyDB tdb = new TinyDB(this);
            Uri uri2= Uri.fromFile(donzo);
            tdb.putString("uriS", uri2.getPath());
            /*
            Glide.with(this)
                    .load(donzo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(NavigationDrawerFragment.iv);
                    */
        } else if (resultCode == Crop.RESULT_ERROR) {
           // Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
        NavigationDrawerFragment.mDrawerLayout.closeDrawer(NavigationDrawerFragment.v);
        finish();
        Communication.SendProfilePic(MainActivity.cpn);

    }
}
