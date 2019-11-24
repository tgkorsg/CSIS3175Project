package com.example.csis3175project.ui.Meme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.csis3175project.Controller.MemeController;
import com.example.csis3175project.Model.Post;
import com.example.csis3175project.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MemeFragment extends Fragment {

    private MemeController MemeController = new MemeController();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_meme, container, false);

        Post post = MemeController.getCurrentClickedMeme();

        return root;
    }
}