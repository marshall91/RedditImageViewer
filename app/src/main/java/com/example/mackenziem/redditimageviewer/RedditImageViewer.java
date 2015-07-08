package com.example.mackenziem.redditimageviewer;

import android.app.Application;
import android.content.Context;

public class RedditImageViewer extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        RedditImageViewer.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return RedditImageViewer.context;
    }
}
