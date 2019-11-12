package com.example.csisproject;

import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    StringBuffer Output = new StringBuffer();
    SQLiteDatabase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDB();
    }

    private void createDB() {
        try {
            DB = openOrCreateDatabase("Meme.db", MODE_PRIVATE, null);
        } catch (Exception ex) {

        }
    }

    private void createTables() {
        // users table
        String createUsers = "CREATE TABLE IF NOT EXISTS users;";
        DB.execSQL((createUsers));

        // favorites table
        String createFavorites = "CREATE TABLE IF NOT EXISTS favorites;";
        DB.execSQL(createFavorites);

    }
}