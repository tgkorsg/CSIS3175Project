package com.example.csis3175project.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.csis3175project.Model.Post;

import java.util.ArrayList;
import java.util.List;

public class DatabaseController extends SQLiteOpenHelper {
    private static DatabaseController Instance;

    private static StringBuffer Output = new StringBuffer();
    private static SQLiteDatabase DB = null;

    private static String DB_NAME = "meme.db";
    private static final int DATABASE_VERSION = 5;

    protected static final String FAVORITE_TABLE = "favorites";

    protected static final String KEY_FAVORITE_ID = "id";
    protected static final String KEY_FAVORITE_IMGUR_ID = "imgur_id";

    private DatabaseController(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);

        DB = getWritableDatabase();
    }

    public static synchronized DatabaseController Initialize(Context context) {
        if (Instance == null) {
            Instance = new DatabaseController(context.getApplicationContext());
        }

        return Instance;
    }

    public static DatabaseController GetInstance() {
        return Instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createFavoriteTable = "CREATE TABLE IF NOT EXISTS " + FAVORITE_TABLE +
                    "(" +
                    KEY_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_FAVORITE_IMGUR_ID + " TEXT UNIQUE" +
                    ")";
            db.execSQL(createFavoriteTable);
        } catch (Exception ex) {
            Output.append(ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_TABLE);
            onCreate(db);
        }
    }

    public boolean AddFavorite(String url) {
        ContentValues favorite = new ContentValues();
        favorite.put(KEY_FAVORITE_IMGUR_ID, url);

        DB = getWritableDatabase();
        DB.beginTransaction();
        try {
            DB.insertOrThrow(FAVORITE_TABLE, null, favorite);
            DB.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            Output.append(ex.getMessage());
            return false;
        } finally {
            DB.endTransaction();
        }
    }

    public boolean RemoveFavorite(String id) {
        DB = getWritableDatabase();
        DB.beginTransaction();
        try {
            DB.delete(FAVORITE_TABLE, KEY_FAVORITE_IMGUR_ID + " = ?", new String[]{id});
            DB.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("DatabaseError", "Cannot delete favorite");
            return false;
        } finally {
            DB.endTransaction();
        }

        return true;
    }

    public List<String> GetFavorites() {
        String getAllUserFavorite =
                "SELECT * FROM " + FAVORITE_TABLE;

        List<String> favoriteList = new ArrayList<>();

        try {
            Cursor cursor = DB.rawQuery(getAllUserFavorite, null);
            if(cursor != null) {
                cursor.moveToFirst();
                do {
                    favoriteList.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Output.append(ex.getMessage());
        }

        return favoriteList;
    }
}
