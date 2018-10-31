package com.jpfeng.baseframework.list;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jpfeng.baseframework.R;
import com.jpfeng.framework.base.ui.list.BaseExtendedAdapter;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/29
 */
public class ListAdapter extends BaseExtendedAdapter<String> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolderCompat(@NonNull ViewGroup parent, int viewType) {
        return new ListItemHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolderCompat(@NonNull RecyclerView.ViewHolder holder, int dataPosition) {
        ((ListItemHolder) holder).tvItem.setText("~~ ##" + holder.getAdapterPosition() + " ~~ String:"
                + mData.get(dataPosition) + " ~~ data#" + dataPosition + " ~~");
    }
}
