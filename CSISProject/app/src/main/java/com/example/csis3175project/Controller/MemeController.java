package com.example.csis3175project.Controller;

import android.util.Log;

import com.example.csis3175project.Model.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MemeController {
    private static List<Post> PostList;

    public MemeController() {
        if(PostList == null) {
            PostList = new ArrayList<>();
        }
    }

    public List<Post> GetMeme() {
        String jsonString = GetImgurData();
        PostList = JsonToArrayOfStringList(jsonString);

        return PostList;
    }

    public String GetImgurData() {
        //String clientID = "ac5733c3af8790b";
        StringBuilder sb = new StringBuilder();

        try {
            String apiURL = "https://api.imgur.com/3/gallery/search/?client_id=546c25a59c58ad7&q=meme"; // https://api.imgur.com/3/gallery/hot/viral/0.json
            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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

    public List<Post> JsonToArrayOfStringList(String json) {
        List<Post> postList = new ArrayList<>();

        try {
            JSONObject jsonObj = new JSONObject(json);
            String data = jsonObj.getString("data");
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject subObj = jsonArray.getJSONObject(i);
                if (subObj.has("images")) {
                    String title = subObj.getString("title");

                    String img = subObj.getString("images");
                    JSONArray jsonArray2 = new JSONArray(img);
                    JSONObject subObj2 = jsonArray2.getJSONObject(0);
                    String url = subObj2.getString("link");

                    if (url.contains(".jpg")) { // load jpg image only
                        postList.add(new Post(url, title));
                    }
                }
            }
        } catch (Exception e) {
            Log.e("MANNY:", e.getMessage());
        }

        return postList;
    }
}
