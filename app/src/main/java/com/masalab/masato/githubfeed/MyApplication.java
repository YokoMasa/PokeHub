package com.masalab.masato.githubfeed;

import android.app.Application;

import com.masalab.masato.githubfeed.githubapi.GitHubApi;

/**
 * Created by Masato on 2018/02/07.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GitHubApi.init(this);
    }

}
