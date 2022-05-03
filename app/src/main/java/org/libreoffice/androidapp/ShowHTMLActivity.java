package org.libreoffice.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import org.libreoffice.androidapp.debug.R;
import org.libreoffice.androidapp.ui.LibreOfficeUIActivity;

/* loaded from: classes.dex */
public class ShowHTMLActivity extends AppCompatActivity {
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_html);
        WebView mWebView = (WebView) findViewById(R.id.browser);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("file:///android_asset/" + getIntent().getStringExtra("path"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: org.libreoffice.androidapp.ShowHTMLActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent intent = new Intent(ShowHTMLActivity.this.getBaseContext(), LibreOfficeUIActivity.class);
                ShowHTMLActivity.this.startActivity(intent);
            }
        });
    }
}
