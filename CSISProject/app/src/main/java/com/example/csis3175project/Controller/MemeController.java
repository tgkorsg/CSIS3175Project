package com.example.csis3175project.Controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.csis3175project.Model.Post;
import com.example.csis3175project.Model.PostMedia;

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

    private static String CLIENT_ID = "a6dc761ea452723";
    private static String CLIENT_SECRET = "ed785970e6d642a35281ed11a1d3825bed1a0ee7";
    private static String AccessToken = null;

    public static String getAccessToken() {
        return AccessToken;
    }

    public static void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public static DatabaseController DatabaseControllerInstance = DatabaseController.GetInstance();

    private static Post CurrentClickedMeme;

    public Post getCurrentClickedMeme() {
        return CurrentClickedMeme;
    }

    public void setCurrentClickedMeme(Post currentClickedMeme) {
        CurrentClickedMeme = currentClickedMeme;
    }

    public static String GetLoginUrl() {
        String url = "https://api.imgur.com/oauth2/authorize?client_id=" +
                CLIENT_ID +
                "&response_type=token&state=login_in";

        return url;
    }

    public MemeController() {
        DatabaseControllerInstance = DatabaseController.GetInstance();

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
                JSONObject postObj = jsonArray.getJSONObject(i);
                if (postObj.has("images")) {
                    // Post
                    Post post = new Post(
                            postObj.getString("id"),
                            postObj.getString("link"),
                            postObj.getString("title"),
                            postObj.getInt("comment_count")
                    );

                    // Image
                    String img = postObj.getString("images");
                    JSONArray jsonArray2 = new JSONArray(img);
                    JSONObject imageJsonObj = jsonArray2.getJSONObject(0);

                    PostMedia postMedia = new PostMedia(
                            imageJsonObj.getString("id"),
                            imageJsonObj.getString("link"),
                            imageJsonObj.getInt("width"),
                            imageJsonObj.getInt("height"),
                            imageJsonObj.getString("type")
                    );

                    post.PostMedia = postMedia;

                    postList.add(post);
                }
            }
        } catch (Exception e) {
            Log.e("MANNY:", e.getMessage());
        }

        return postList;
    }

    public static void AddFavorite(Post post) {
        DatabaseControllerInstance.AddFavorite(post.PostMedia.ID);
    }
}
