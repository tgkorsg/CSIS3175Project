package com.example.csisproject;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.example.csisproject.Model.Post;

import java.util.List;

public class MainActivity extends BaseActivity {
    //DBController DBControllerInstance = DBController.GetInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateDB();
        CreateTable();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final String data = getImgurData();
                    List<String>[] list = jsonToArrayOfStringList(data);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView txtView = findViewById(R.id.txtView);
                            txtView.setText(data);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


                // if click img in listView, [TEMPORARY]
                startActivity(new Intent(MainActivity.this, MemeActivity.class));
            }
        });
    }
}