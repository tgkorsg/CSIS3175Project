package com.example.csisproject;
import android.os.Bundle;

public class MainActivity extends BaseActivity {
    //DBController DBControllerInstance = DBController.GetInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        CreateDB();
        CreateTable();
    }
}