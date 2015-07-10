package com.example.mackenziem.redditimageviewer;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewClient extends WebViewClient {
    private String setUrl;

    public CustomWebViewClient(String url) {
        setUrl = url;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(url.equals(setUrl)){
            view.loadUrl(url);
        }
        return true;
    }
}
