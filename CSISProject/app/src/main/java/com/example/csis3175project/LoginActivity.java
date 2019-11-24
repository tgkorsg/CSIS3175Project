package com.example.csis3175project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.CookieManager;
import android.webkit.WebView;

import com.example.csis3175project.Controller.MemeController;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = sharedPreferences.getString("accesstoken", "");
        if(accessToken != null && !accessToken.isEmpty()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            setContentView(R.layout.activity_login);

            CookieManager.getInstance().setAcceptCookie(true);

            WebView webView = findViewById(R.id.loginWebView);
            webView.clearCache(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new LoginWebViewClient(this));
            webView.loadUrl(MemeController.GetLoginUrl());
        }
    }
}
