package com.example.csisproject;

import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csisproject.Controller.DBController;

public class MainActivity extends AppCompatActivity {
    DBController DBControllerInstance = DBController.GetInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.testTitle);

        if(DBControllerInstance == null) {
            Toast.makeText(this, "Database not created", Toast.LENGTH_LONG);
            textView.setText("False");
        } else {
            Toast.makeText(this, "Database exists or created", Toast.LENGTH_LONG);
            textView.setText("True");
        }
    }
}