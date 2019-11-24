package com.example.csis3175project.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.csis3175project.Controller.MemeController;
import com.example.csis3175project.Model.Post;
import com.example.csis3175project.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MemeAdapter extends BaseAdapter {
    List<Post> MemeList;
    Context Context;

    public MemeAdapter(List<Post> postList, Context context) {
        super();
        this.MemeList = postList;
        this.Context = context;
    }

    @Override
    public int getCount() {
        return this.MemeList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.MemeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.Context);
            view = layoutInflater.inflate(R.layout.meme_item, parent, false);
        } else {
            view = convertView;
        }

        final Post meme = this.MemeList.get(position);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) this.Context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int screenWidth = displayMetrics.widthPixels;

        TextView memeTitle = view.findViewById(R.id.memeTitle);
        memeTitle.setText(meme.Title);

        final ImageView memeImg = view.findViewById(R.id.memeImg);
        final ImageView favoriteIcon = view.findViewById(R.id.favoriteIcon);
        favoriteIcon.setImageResource(R.raw.heart);

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            int currentFavState = R.raw.heart_filled;

            @Override
            public void onClick(View v) {
                MemeController.AddFavorite(meme);

                if(currentFavState == R.raw.heart) {
                    currentFavState = R.raw.heart_filled;
                } else  {
                    currentFavState = R.raw.heart;
                }

                favoriteIcon.setImageResource(currentFavState);
            }
        });

        try {
            double ratio = (double)screenWidth / (double)meme.PostMedia.Width;
            Picasso.get().load(meme.PostMedia.Url)
                    .resize(screenWidth, (int)(meme.PostMedia.Height * ratio))
                    .into(memeImg);
        } catch (Exception e) {
            Log.d("Catch", "Fail to load image");
        }

        return view;
    }
}
