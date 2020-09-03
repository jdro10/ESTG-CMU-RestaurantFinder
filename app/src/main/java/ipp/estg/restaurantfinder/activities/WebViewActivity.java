package ipp.estg.restaurantfinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import ipp.estg.restaurantfinder.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        this.web = (WebView) findViewById(R.id.webView);
        this.web.getSettings().setJavaScriptEnabled(true);
        this.web.loadUrl("https://www.google.com");
    }
}