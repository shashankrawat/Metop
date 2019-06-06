package com.elintminds.mac.metatopos.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.common.DialogUtils;

public class TermContionsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView term_conditions_backbtn;
    WebView webview;
    ProgressDialog m_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_contions);
        term_conditions_backbtn = findViewById(R.id.term_conditions_backbtn);
        webview = findViewById(R.id.webview);
        m_Dialog = DialogUtils.showProgressDialog(this);
        m_Dialog.show();
        webview.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.loadUrl("http://metatopos.elintminds.work/pages/terms-and-conditions");
        term_conditions_backbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == term_conditions_backbtn) {
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
