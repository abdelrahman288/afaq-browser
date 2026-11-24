package org.afaq.browser;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.Context;
import android.net.Uri;

public class MainActivity extends Activity {
    private WebView web;
    private EditText address;
    private ProgressBar progress;
    private Button back, forward, reload, go;
    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);

        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        address = new EditText(this);
        address.setSingleLine(true);
        address.setHint("Search or enter URL");
        address.setImeOptions(EditorInfo.IME_ACTION_GO);
        bar.addView(address, new LinearLayout.LayoutParams(0, dp(48), 1));

        go = button(this, "Go");
        bar.addView(go);
        root.addView(bar);

        LinearLayout controls = new LinearLayout(this);
        back = button(this, "‹");
        forward = button(this, "›");
        reload = button(this, "↻");
        controls.addView(back); controls.addView(forward); controls.addView(reload);
        root.addView(controls);

        progress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progress.setMax(100);
        root.addView(progress, new LinearLayout.LayoutParams(-1, dp(3)));

        web = new WebView(this);
        root.addView(web, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);

        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        web.setWebViewClient(new WebViewClient() {
            @Override public void onPageFinished(WebView view, String url) {
                address.setText(url);
                back.setEnabled(web.canGoBack());
                forward.setEnabled(web.canGoForward());
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override public void onProgressChanged(WebView view, int p) {
                progress.setProgress(p);
                progress.setVisibility(p >= 100 ? View.GONE : View.VISIBLE);
            }
        });
        View.OnClickListener navigate = v -> loadAddress();
        go.setOnClickListener(navigate);
        address.setOnEditorActionListener((v, action, event) -> {
            if(action == EditorInfo.IME_ACTION_GO || action == EditorInfo.IME_ACTION_DONE) { loadAddress(); return true; }
            return false;
        });
        back.setOnClickListener(v -> { if(web.canGoBack()) web.goBack(); });
        forward.setOnClickListener(v -> { if(web.canGoForward()) web.goForward(); });
        reload.setOnClickListener(v -> web.reload());
        web.loadUrl("https://www.google.com");
    }

    private Button button(Context c, String text) {
        Button b = new Button(c);
        b.setText(text);
        b.setAllCaps(false);
        return b;
    }
    private void loadAddress() {
        String s = address.getText().toString().trim();
        if(s.isEmpty()) return;
        if(s.matches("^[a-zA-Z][a-zA-Z0-9+.-]*://.*")) web.loadUrl(s);
        else if(s.matches("^[^\\s.]+$") || s.contains(" ")) web.loadUrl("https://www.google.com/search?q=" + Uri.encode(s));
        else web.loadUrl("https://" + s);
    }
    @Override public void onBackPressed() {
        if(web != null && web.canGoBack()) web.goBack(); else super.onBackPressed();
    }
    @Override protected void onPause() { if(web != null) web.onPause(); super.onPause(); }
    @Override protected void onResume() { super.onResume(); if(web != null) web.onResume(); }
    @Override protected void onDestroy() {
        if(web != null) { web.stopLoading(); web.loadUrl("about:blank"); web.clearHistory(); web.removeAllViews(); web.destroy(); web = null; }
        super.onDestroy();
    }
}