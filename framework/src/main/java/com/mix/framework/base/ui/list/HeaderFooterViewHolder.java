package com.mix.framework.base.ui.list;

import android.view.View;
import android.widget.FrameLayout;

import com.mix.framework.R;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/10
 */
class HeaderFooterViewHolder extends RecyclerView.ViewHolder {

    FrameLayout mFlRoot;

    HeaderFooterViewHolder(View itemView) {
        super(itemView);
        mFlRoot = itemView.findViewById(R.id.fl_item_header_footer_root);
    }
}
