package com.example.csisproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MemeActivity extends AppCompatActivity {
    String imageUrl;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme);
        // !!!get selected url from intent!!!
        // imageUrl = ...;

        // Display Image
        imageUrl = "https://i.imgur.com/tGbaZCY.jpg"; // [TEST IMAGE]
        ImageView imgView = findViewById(R.id.imgViewLarge);
        Picasso.with(this).load(imageUrl).into(imgView);

        // Save Image
        saveBtn = findViewById(R.id.btnSaveGallery);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveImage(imageUrl);
                } catch (Exception e) {
                    Log.e("MANNY:",e.getMessage());
                }
            }
        });
    }

    private void saveImage(String imageUrl) throws IOException {
        URL url = new URL (imageUrl);
        InputStream input = url.openStream();
        try {
            //The sdcard directory e.g. '/sdcard' can be used directly, or
            //more safely abstracted with getExternalStorageDirectory()
            File storagePath = Environment.getExternalStorageDirectory();
            OutputStream output = new FileOutputStream (storagePath + "/myImage2.jpg");
            try {
                byte[] buffer = new byte[2048];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }
            } finally {
                output.close();
            }
        } finally {
            input.close();
        }
    }
}
