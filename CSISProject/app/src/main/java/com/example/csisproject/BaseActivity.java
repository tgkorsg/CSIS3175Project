package com.example.csisproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csisproject.Model.Favorite;
import com.example.csisproject.Model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    private static StringBuffer Output = new StringBuffer();
    private static SQLiteDatabase DB = null;
    private static String DBName = "meme.db";

    protected static final String UserTable = "users";
    protected static final String FavoriteTable = "favorites";

    protected static final String KEY_USER_ID = "id";
    protected static final String KEY_USER_NAME = "name";
    protected static final String KEY_USER_USERNAME = "username";
    protected static final String KEY_USER_PASSWORD = "password";

    protected static final String KEY_FAVORITE_ID = "id";
//    protected static final String KEY_FAVORITE_USERID = "userID";
    protected static final String KEY_FAVORITE_URL = "url";

    public void CreateDB() {
        if(DB == null) {
            try {
                DB = openOrCreateDatabase(DBName, MODE_PRIVATE, null);
                Log.d("DB","Opened or created database");
            } catch (Exception ex) {
                Log.d("DB", "Error open or create db");
                Toast.makeText(this, "Error open or create db", Toast.LENGTH_LONG);
                Output.append(ex.getMessage());
            }
        }
    }

    protected void CreateTable() {
        try {
//            String createUserTable = "CREATE TABLE IF NOT EXISTS " + UserTable +
//                    "(" +
//                    KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    KEY_USER_NAME + " TEXT," +
//                    KEY_USER_USERNAME + " TEXT," +
//                    KEY_USER_PASSWORD + " TEXT" +
//                    ")";

            String createFavoriteTable = "CREATE TABLE IF NOT EXISTS " + FavoriteTable +
                    "(" +
                    KEY_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    KEY_FAVORITE_USERID + " TEXT," +
                    KEY_FAVORITE_URL + " TEXT" +
                    ")";

//            DB.execSQL(createUserTable);
            DB.execSQL(createFavoriteTable);
        } catch (Exception ex) {
            Output.append(ex.getMessage());
        }
    }

    protected boolean AddUser(User data, String password) {
        ContentValues user = new ContentValues();
        user.put(KEY_USER_NAME, data.Name);
        user.put(KEY_USER_PASSWORD, password);
        user.put(KEY_USER_USERNAME, data.Username);

        try {
            DB.insert(UserTable, null, user);
            return true;
        } catch (Exception ex) {
            Output.append(ex.getMessage());
            return false;
        }
    }

    protected boolean AddFavoriteMeme(Favorite data) {
        ContentValues favorite = new ContentValues();
        favorite.put(KEY_FAVORITE_URL, data.URL);
//        favorite.put(KEY_FAVORITE_USERID, data.UserID);

        try {
            DB.insert(FavoriteTable, null, favorite);
            return true;
        } catch (Exception ex) {
            Output.append(ex.getMessage());
            return false;
        }
    }

    private List<Favorite> GetAllUserFavorite(String userID) {
        String getAllUserFavorite =
                "SELECT * FROM " + FavoriteTable;// +
//                " WHERE " + KEY_FAVORITE_USERID +
//                " = " + useID;

        List<Favorite> favoriteList = new ArrayList<>();

        try {
            Cursor cursor = DB.rawQuery(getAllUserFavorite, null);
            if(cursor != null) {
                cursor.moveToFirst();
                do {
                    Favorite temp = new Favorite();
                    temp.ID = cursor.getString(0);
//                    temp.UserID = cursor.getString(1);
                    temp.URL = cursor.getString(2);

                    favoriteList.add(temp);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Output.append(ex.getMessage());
        }

        return favoriteList;
    }

//    private boolean Login(User user) {
//        String getUserByUsername =
//                "SELECT * FROM " + UserTable +
//                " WHERE " + KEY_USER_USERNAME +
//                " = " + user.Username;
//    }

    protected String getImgurData() {
        String clientID = "ac5733c3af8790b";
        StringBuilder sb = new StringBuilder();

        try {
            String apiURL = "https://api.imgur.com/3/gallery/search/?client_id=546c25a59c58ad7&q=meme"; // only page0 works. maybe change sort to top, and window to month
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