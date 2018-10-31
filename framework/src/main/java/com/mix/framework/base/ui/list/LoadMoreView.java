package com.mix.framework.base.ui.list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/30
 */
public abstract class LoadMoreView extends FrameLayout {
    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void showNormal();
    public abstract void showLoading();
    public abstract void showError(String errorMessage);
    public abstract void showNoMore();
}
