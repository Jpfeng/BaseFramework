package com.jpfeng.framework.base.ui.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jpfeng.framework.R;

import androidx.annotation.Nullable;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/30
 */
public class DefaultLoadMoreView extends LoadMoreView {

    private ProgressBar mPbProgress;
    private TextView mTvHint;

    public DefaultLoadMoreView(Context context) {
        this(context, null);
    }

    public DefaultLoadMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context)
                .inflate(R.layout.view_loading_more, this, true);
        mPbProgress = findViewById(R.id.pb_loading_more_progress);
        mTvHint = findViewById(R.id.tv_loading_more_hint);
    }

    @Override
    public void showNormal() {
        mPbProgress.setVisibility(View.GONE);
        mTvHint.setText(getContext().getString(R.string.loading_more));
    }

    @Override
    public void showLoading() {
        mPbProgress.setVisibility(View.VISIBLE);
        mTvHint.setText(getContext().getString(R.string.loading_more_loading));
    }

    @Override
    public void showError(String errorMessage) {
        mPbProgress.setVisibility(View.GONE);
        mTvHint.setText(errorMessage);
    }

    @Override
    public void showNoMore() {
        mPbProgress.setVisibility(View.GONE);
        mTvHint.setText(getContext().getString(R.string.loading_more_end));
    }
}
