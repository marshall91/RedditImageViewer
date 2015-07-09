package com.example.mackenziem.redditimageviewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.android.volley.toolbox.NetworkImageView;

public class CommentsFragment extends Fragment {
    private static final String LOGTAG = "COMM_FRAG";

    private String commentUrl;
    private String imgUrl;

    public CommentsFragment() {}

    public void setCommentUrl(String url) {
        commentUrl = url;
    }
    public void setImgUrl(String url) { imgUrl = url; }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_comments, container, false);

        NetworkImageView myNetImgView = (NetworkImageView) rootView.findViewById(R.id.imageview);
        myNetImgView.setImageUrl(imgUrl, VolleySingleton.getInstance().getImageLoader());

        WebView myWebView = (WebView) rootView.findViewById(R.id.webview);
        myWebView.loadUrl(commentUrl);

        return rootView;
    }
}
