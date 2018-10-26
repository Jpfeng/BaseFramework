package com.mix.framework.base.ui.list;

import android.content.Context;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/29
 */
public abstract class RefreshView extends FrameLayout {
    public RefreshView(@NonNull Context context) {
        this(context, null);
    }

    public RefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void pulling(@FloatRange(from = 0f, to = 1f) float progress);
    public abstract void startRefresh();
    public abstract void stopRefresh();
}
