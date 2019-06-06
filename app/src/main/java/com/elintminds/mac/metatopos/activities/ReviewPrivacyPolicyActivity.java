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

public class ReviewPrivacyPolicyActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView privay_polocies_backbtn;
WebView privacy_policy_webview;
ProgressDialog m_Dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_privacy_policy);
        privay_polocies_backbtn=findViewById(R.id.privay_polocies_backbtn);
        privay_polocies_backbtn.setOnClickListener(this);
        privacy_policy_webview = findViewById(R.id.privacy_policies_webview);
        privacy_policy_webview.setWebViewClient(new MyWebViewClient());
        m_Dialog = DialogUtils.showProgressDialog(this);
        m_Dialog.show();
        WebSettings webSettings = privacy_policy_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        privacy_policy_webview.loadUrl("http://metatopos.elintminds.work/pages/privacy-policy");
    }

    @Override
    public void onClick(View view) {

        if (view==privay_polocies_backbtn)
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
