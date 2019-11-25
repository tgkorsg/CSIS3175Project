package com.example.csis3175project.ui.Meme;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.csis3175project.Controller.MemeController;
import com.example.csis3175project.MainActivity;
import com.example.csis3175project.Model.Post;
import com.example.csis3175project.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.File;

public class MemeFragment extends Fragment {

    private MemeController MemeController = new MemeController();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_meme, container, false);

        Post post = MemeController.getCurrentClickedMeme();

        // Display Image
        final String imgUrl = post.URL;
        final ImageView imgView = root.findViewById(R.id.imgViewLarge);
        Picasso.get().load(imgUrl).into(imgView);

        // Save Image
        final String imgId = post.ID;
        Button saveBtn = root.findViewById(R.id.btnSaveGallery);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MemeController.saveImage(imgView, imgId);
                } catch (Exception e) {
                    Log.e("MANNY:", e.getMessage());
                }
            }
        });

        // Share Image
        Button shareBtn = root.findViewById(R.id.btnShareImg);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/jpg");
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(
                            new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + imgId + ".jpg")));
                    startActivity(Intent.createChooser(shareIntent, "Share this meme with..."));
                } catch (Exception e) {
                    Log.e("MANNY:", e.getMessage());
                }
            }
        });

        return root;
    }
}