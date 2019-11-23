package com.example.csis3175project;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.csis3175project.Model.Post;
import com.example.csis3175project.Model.User;

import android.util.Log;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_meme_list, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

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

    protected boolean AddFavoriteMeme(Post data) {
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

    private List<Post> GetAllUserFavorite(String userID) {
        String getAllUserFavorite =
                "SELECT * FROM " + FavoriteTable;// +
//                " WHERE " + KEY_FAVORITE_USERID +
//                " = " + useID;

        List<Post> favoriteList = new ArrayList<>();

        try {
            Cursor cursor = DB.rawQuery(getAllUserFavorite, null);
            if(cursor != null) {
                cursor.moveToFirst();
                do {
                    Post temp = new Post();
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
}
