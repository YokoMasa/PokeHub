package com.masalab.masato.githubfeed.view;

import android.graphics.Bitmap;

/**
 * Created by Masato on 2018/02/25.
 */

public interface ContentView extends BaseView {
    public void showBitmapContent(Bitmap bitmap);

    public void showHtmlContent(String html);

    public void showCodeContent(String code);
}
