package com.mix.framework.base.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mix.framework.R;

import butterknife.ButterKnife;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/10
 */
class LoadingMoreViewHolder extends RecyclerView.ViewHolder {

    LinearLayout mLlRoot;
    ProgressBar mPbProgress;
    TextView mTvHint;

    LoadingMoreViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mLlRoot = itemView.findViewById(R.id.ll_item_loading_more_root);
        mPbProgress = itemView.findViewById(R.id.pb_item_loading_more_progress);
        mTvHint = itemView.findViewById(R.id.tv_item_loading_more_hint);
    }
}
