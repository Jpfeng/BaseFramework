package com.mix.framework.base.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.mix.framework.R;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/10
 */
class HeaderViewHolder extends RecyclerView.ViewHolder {

    FrameLayout mFlRoot;

    HeaderViewHolder(View itemView) {
        super(itemView);
        mFlRoot = itemView.findViewById(R.id.fl_item_header_root);
    }
}
