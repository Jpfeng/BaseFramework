package com.jpfeng.baseframework.list;

import android.view.View;
import android.widget.TextView;

import com.jpfeng.baseframework.R;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/29
 */
public class ListItemHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_list_item)
    public TextView tvItem;

    public ListItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
