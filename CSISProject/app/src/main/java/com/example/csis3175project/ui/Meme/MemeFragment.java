package com.example.csis3175project.ui.Meme;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

        final Post post = MemeController.getCurrentClickedMeme();

        // Display Image
        final String imgUrl = post.URL;
        final ImageView imgView = root.findViewById(R.id.imgViewLarge);
        Picasso.get().load(imgUrl).into(imgView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int screenWidth = displayMetrics.widthPixels;

        LinearLayout memeImageContainer = root.findViewById(R.id.memeImageContainer);
        ViewGroup.LayoutParams params = memeImageContainer.getLayoutParams();
        params.height = screenWidth;
        params.width = screenWidth;
        memeImageContainer.setLayoutParams(params);

        try {
            double ratio = (double)screenWidth / (double)post.PostMedia.Height;
            Picasso.get().load(post.PostMedia.Url)
                    .resize((int)(post.PostMedia.Width * ratio), screenWidth)
                    .into(imgView);
        } catch (Exception e) {
            Log.d("Catch", "Fail to load image");
        }

        // Save Image
        final String imgId = post.ID;
        Button saveBtn = root.findViewById(R.id.btnSaveGallery);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final boolean success = MemeController.saveImage(imgView, post);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(success) {
                                        Toast.makeText(getActivity(), "Saved in Download folder", Toast.LENGTH_SHORT);
                                    } else {
                                        Toast.makeText(getActivity(), "Error saving", Toast.LENGTH_SHORT);
                                    }
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        Button setWallpaperBtn = root.findViewById(R.id.bntWallpaper);
        setWallpaperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                      final boolean success = MemeController.SetWallpaper(post, getActivity());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(success) {
                                Toast.makeText(root.getContext(), "Wallpaper is set", Toast.LENGTH_SHORT);
                            } else {
                                Toast.makeText(root.getContext(), "Error setting wallpaper", Toast.LENGTH_SHORT);
                            }
                        }
                    });

                    }
                });

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