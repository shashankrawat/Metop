package com.elintminds.mac.metatopos.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.common.DialogUtils;

public class ViewAdvertisementLinkActivity extends AppCompatActivity implements View.OnClickListener {
    WebView webview;
    ProgressDialog m_Dialog;
    ImageView backbtn_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_advertisement_link);
        webview = findViewById(R.id.external_link_webview);
        backbtn_icon=findViewById(R.id.backbtn_icon);
        backbtn_icon.setOnClickListener(this);
        Intent intent = getIntent();
        String link = intent.getStringExtra("External_Link");
        m_Dialog = DialogUtils.showProgressDialog(this);
        m_Dialog.show();
        webview.setWebViewClient(new ViewAdvertisementLinkActivity.MyWebViewClient());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.loadUrl(link);


    }

    @Override
    public void onClick(View view) {
        if (view==backbtn_icon)
        {
            finish();
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            if (!m_Dialog.isShowing()) {
                m_Dialog.show();
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (m_Dialog.isShowing()) {
                m_Dialog.dismiss();
            }


        }
    }
}
