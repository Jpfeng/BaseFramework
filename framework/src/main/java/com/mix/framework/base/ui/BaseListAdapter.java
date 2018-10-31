package com.mix.framework.base.ui;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/10
 */
public abstract class BaseListAdapter<T> extends RecyclerView.Adapter {
    protected List<T> mData;

    public BaseListAdapter() {
        mData = new ArrayList<>();
    }

    /**
     * 重新设置数据。会首先将数据集清空。
     *
     * @param newData 新的数据
     */
    public void setNewData(@Nullable List<T> newData) {
        mData.clear();
        if (null != newData) {
            mData.addAll(newData);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return null == mData ? 0 : mData.size();
    }
}
