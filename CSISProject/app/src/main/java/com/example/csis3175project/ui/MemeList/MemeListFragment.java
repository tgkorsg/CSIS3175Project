package com.example.csis3175project.ui.MemeList;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.csis3175project.Adapter.MemeAdapter;
import com.example.csis3175project.Controller.MemeController;
import com.example.csis3175project.Model.Post;
import com.example.csis3175project.R;

import java.util.List;

public class MemeListFragment extends Fragment {

    private MemeController MemeController = new MemeController();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_meme_list, container, false);

        root.setBackgroundColor(Color.GRAY);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final String data = MemeController.GetImgurData();
                    final List<Post> memeList = MemeController.JsonToArrayOfStringList(data);
                    MemeController.UpdatePostIDList();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MemeAdapter memeAdapter = new MemeAdapter(memeList, root.getContext());

                            ListView memeListView = root.findViewById(R.id.memeListView);
                            memeListView.setAdapter(null);
                            memeListView.setAdapter(memeAdapter);

                            memeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    MemeController.setCurrentClickedMeme(memeList.get(position));
                                    Navigation.findNavController(view).navigate(R.id.onMemeClick);
                                }
                            });
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        return root;
    }
}