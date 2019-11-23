package com.example.csis3175project.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        final int width = displayMetrics.widthPixels;

        TextView memeTitle = view.findViewById(R.id.memeTitle);
        memeTitle.setText(meme.Title);

        final ImageView memeImg = view.findViewById(R.id.memeImg);

        try {
            Picasso.get().load(meme.URL).into(memeImg);
        } catch (Exception e) {
            Log.d("Catch", "Fail to load image");
        }

        return view;
    }
}
