package ipp.estg.restaurantfinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import ipp.estg.restaurantfinder.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView web;
    private TextView urlTextView;
    private ImageView closeImage;
    private String urlIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent urlIntent = getIntent();
        this.urlIntent = urlIntent.getStringExtra("url");

        this.urlTextView = findViewById(R.id.web_view_url_text);
        this.closeImage = findViewById(R.id.close_img);

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewActivity.super.onBackPressed();
            }
        });

        this.web = (WebView) findViewById(R.id.webView);
        this.web.getSettings().setJavaScriptEnabled(false);

        this.web.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url){
                urlTextView.setText(url);
            }
        });

        this.web.loadUrl(this.urlIntent);
    }
}