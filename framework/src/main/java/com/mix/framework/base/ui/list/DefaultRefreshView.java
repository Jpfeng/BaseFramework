package com.mix.framework.base.ui.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mix.framework.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/30
 */
public class DefaultRefreshView extends RefreshView {

    private ProgressBar mPbProgress;
    private TextView mTvHint;

    public DefaultRefreshView(@NonNull Context context) {
        this(context, null);
    }

    public DefaultRefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context)
                .inflate(R.layout.view_refresh, this, true);
        mPbProgress = findViewById(R.id.pb_refresh_progress);
        mTvHint = findViewById(R.id.tv_refresh_hint);
    }

    @Override
    public void pulling(float progress) {
        mPbProgress.setVisibility(INVISIBLE);
        mTvHint.setVisibility(VISIBLE);
        mTvHint.setText(String.valueOf(progress));
    }

    @Override
    public void startRefresh() {
        mPbProgress.setVisibility(VISIBLE);
        mTvHint.setVisibility(INVISIBLE);
    }

    @Override
    public void stopRefresh() {
        mPbProgress.setVisibility(INVISIBLE);
        mTvHint.setVisibility(VISIBLE);
        mTvHint.setText("0");
    }
}
