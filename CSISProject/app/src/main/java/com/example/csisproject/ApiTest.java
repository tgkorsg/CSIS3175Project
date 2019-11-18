package com.example.csisproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final String data = getImgurData();

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
            }
        });
    }

    private String getImgurData() {
        String clientID = "ac5733c3af8790b";
        StringBuilder sb = new StringBuilder();

        try {
            String apiURL = "https://api.imgur.com/3/gallery/hot/viral/0.json"; // only page0 works. maybe change sort to top, and window to month

            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", clientID);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream())); // error here!!!
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            br.close();
            connection.disconnect();
        } catch (Exception e) {
            return e.toString();
        }

        return sb.toString();
    }
}
