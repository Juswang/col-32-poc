package org.libreoffice.androidlib;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

/* loaded from: classes3.dex */
public class SlideShowActivity extends AppCompatActivity {
    static final String SVG_URI_KEY = "svgUriKey";
    private static final String TAG = "SlideShowActivity";
    private WebView slideShowWebView;
    private String slidesSvgUri;

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lolib_activity_slide_show);
        this.slideShowWebView = (WebView) findViewById(R.id.slide_show_webView);
        if (savedInstanceState == null) {
            this.slidesSvgUri = getIntent().getStringExtra(SVG_URI_KEY);
        } else {
            this.slidesSvgUri = savedInstanceState.getString(SVG_URI_KEY);
        }
        Log.d(TAG, "SlideShow Svg Uri " + this.slidesSvgUri);
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isChromeDebugEnabled = sPrefs.getBoolean("ENABLE_CHROME_DEBUGGING", false);
        if ((getApplicationInfo().flags & 2) != 0 || isChromeDebugEnabled) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        this.slideShowWebView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        WebSettings slideShowWebViewSettings = this.slideShowWebView.getSettings();
        slideShowWebViewSettings.setLoadWithOverviewMode(true);
        slideShowWebViewSettings.setLoadsImagesAutomatically(true);
        slideShowWebViewSettings.setUseWideViewPort(true);
        slideShowWebViewSettings.setJavaScriptEnabled(true);
        slideShowWebViewSettings.setSupportZoom(true);
        slideShowWebViewSettings.setBuiltInZoomControls(true);
        slideShowWebViewSettings.setAllowFileAccess(true);
        this.slideShowWebView.loadUrl(this.slidesSvgUri);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SVG_URI_KEY, this.slidesSvgUri);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(5894);
        }
    }
}
