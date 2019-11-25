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

    private static List<String> PostIDList;

    private static String CurrentMemeType = "time";

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
            String apiURL = "https://api.imgur.com/3/gallery/search/" +
                    CurrentMemeType + "/" +
                    "?client_id=" +
                    CLIENT_ID +
                    "&q=meme";
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

    public Post GetImgurGallery(String id) {
        String apiURL = "https://api.imgur.com/3/gallery/album/" + id;

        try {
            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);

            String rawData = MakeImgurApiRequest(connection);
            JSONObject rawJson = new JSONObject(rawData);
            Post post = ParseGallery(rawJson.getJSONObject("data"));

            return post;
        } catch (Exception e) {
            return new Post("", "", "Cannot Load Post", 0);
        }
    }

    public String MakeImgurApiRequest(HttpURLConnection connection) {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            br.close();
            connection.disconnect();
        } catch (Exception e) {
            return "";
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
                    postList.add(ParseGallery(postObj));
                }
            }
        } catch (Exception e) {
            Log.e("MANNY:", e.getMessage());
        }

        return postList;
    }

    private Post ParseGallery(JSONObject postObj) {
        try {
            if (postObj.has("images")) {
                // Post
                Post post = new Post(
                        postObj.getString("id"),
                        postObj.getString("link"),
                        postObj.getString("title"),
                        postObj.getInt("comment_count")
                );

                // Image
                JSONArray images = postObj.getJSONArray("images");
                JSONObject imageJsonObj = images.getJSONObject(0);

                PostMedia postMedia = new PostMedia(
                        imageJsonObj.getString("id"),
                        imageJsonObj.getString("link"),
                        imageJsonObj.getInt("width"),
                        imageJsonObj.getInt("height"),
                        imageJsonObj.getString("type")
                );

                post.PostMedia = postMedia;

                return post;
            }
        }  catch (Exception ex){
            Log.e("MANNY:", ex.getMessage());
        }

        return new Post("", "", "Cannot Load Post", 0);
    }

    public static void SetTop() {
        CurrentMemeType = "top";
    }

    public static void SetTime() {
        CurrentMemeType = "time";
    }

    public static void SetViral() {
        CurrentMemeType = "viral";
    }

    // Database
    public void AddFavorite(Post post) {
        DatabaseControllerInstance.AddFavorite(post.ID);
    }

    public void RemoveFavorite(Post post) {
        DatabaseControllerInstance.RemoveFavorite(post.ID);
    }

    public boolean CheckIfIsFavorite(String id) {
        if(PostIDList == null) {
            return false;
        }

        return PostIDList.contains(id);
    }

    public void UpdatePostIDList() {
        List<String> idList = DatabaseControllerInstance.GetFavorites();
        PostIDList = idList;
    }

    public List<Post> GetFavorite() {
        List<String> idList = DatabaseControllerInstance.GetFavorites();
        PostIDList = idList;

        List<Post> postList = new ArrayList<>();

        for(String id: idList) {
            postList.add(GetImgurGallery(id));
        }

        return postList;
    }
}
