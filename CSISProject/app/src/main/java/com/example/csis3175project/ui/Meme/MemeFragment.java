package com.example.csis3175project.ui.Meme;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.csis3175project.Adapter.MemeAdapter;
import com.example.csis3175project.Controller.MemeController;
import com.example.csis3175project.Model.Post;
import com.example.csis3175project.R;
import com.example.csis3175project.ui.MemeList.HomeViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class MemeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_meme, container, false);

        return root;
    }
}