package com.example.csis3175project;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.csis3175project.Controller.MemeController;

class LoginWebViewClient extends WebViewClient {

    LoginActivity mDelegate;

    public LoginWebViewClient(LoginActivity ref) {
        this.mDelegate = ref;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // perform task
        String cookies = CookieManager.getInstance().getCookie(url);
        String[] cookies2 = cookies.split(";");

        // Find Keys placed in browser cookies.
        for (String c : cookies2) {
            String[] c2 = c.split("=");

            if (c2[0].equals(" accesstoken")) {
                String accessToken = c2[1];
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mDelegate);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("accesstoken", accessToken);
                editor.commit();
                MemeController.setAccessToken(accessToken);
                return;
            }
        }
    }

}
