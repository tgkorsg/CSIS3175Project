package com.example.csisproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

public class MemeActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme);

        verifyStoragePermissions(MemeActivity.this); // prompt user write-permission if not yet granted

        // !!!get selected url from intent!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!use putextra from last activity!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // imageUrl = ...;

        // Display Image
        final String imageUrl = "https://i.imgur.com/tGbaZCY.jpg"; // [TEST IMAGE]
        final ImageView imgView = findViewById(R.id.imgViewLarge);
        Picasso.with(this).load(imageUrl).into(imgView);

        // Save Image
        Button saveBtn = findViewById(R.id.btnSaveGallery);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveImage(imgView, parseUrl(imageUrl));
                } catch (Exception e) {
                    Log.e("MANNY:", e.getMessage());
                }
            }
        });

        // share Image
        Button shareBtn = findViewById(R.id.btnShareImg);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/jpg");
                    Log.d("MANNY:", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + parseUrl(imageUrl));
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + parseUrl(imageUrl))));
                    startActivity(Intent.createChooser(shareIntent, "Share this meme with..."));
                } catch (Exception e) {
                    Log.e("MANNY:", e.getMessage());
                }
            }
        });
    }

    private void saveImage(View v,String filename){
        String StoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String savePath = StoragePath + "/Download";
        File f = new File(savePath);
        if (!f.isDirectory()) f.mkdirs();
        v.buildDrawingCache();
        Bitmap bitmap = v.getDrawingCache();
        FileOutputStream fos;

        try{
            fos = new FileOutputStream(savePath+"/"+filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Image NOT Saved", Toast.LENGTH_SHORT).show();
            Log.e("MANNY:", e.getMessage());
        }
    }

    // get write external storage permission
    private void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    // get unique id from url
    private String parseUrl(String url) {
        String[] tokens = url.split("/");
        return tokens[3];
    }
}
