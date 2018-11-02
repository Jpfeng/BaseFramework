package com.jpfeng.baseframework.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jpfeng.baseframework.R;
import com.jpfeng.baseframework.bean.DemoBean;
import com.jpfeng.framework.base.ui.BaseListAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2018/11/2
 */
public class DemoAdapter extends BaseListAdapter<DemoBean, DemoAdapter.DemoItemHolder> {

    private ItemClickListener mListener;

    public DemoAdapter(@NonNull ItemComparator<DemoBean> comparator) {
        super(comparator);
    }

    @NonNull
    @Override
    public DemoItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DemoItemHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_demo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DemoItemHolder holder, int position) {
        DemoBean bean = mData.get(position);
        Glide.with(holder.ivImage).asBitmap().load(bean.getImage()).into(holder.ivImage);
        holder.tvTitle.setText(bean.getDesc());
        holder.tvAuthor.setText(bean.getWho());
        holder.tvCategory.setText(bean.getType());
        holder.tvTime.setText(bean.getPublishedAt());
        holder.itemView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onItemClicked(holder.getAdapterPosition());
            }
        });
    }

    public void setOnItemClickListener(@NonNull ItemClickListener listener) {
        mListener = listener;
    }

    public interface ItemClickListener {
        void onItemClicked(int position);
    }

    static class DemoItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_item_image)
        ImageView ivImage;
        @BindView(R.id.tv_item_title)
        TextView tvTitle;
        @BindView(R.id.tv_item_author)
        TextView tvAuthor;
        @BindView(R.id.tv_item_category)
        TextView tvCategory;
        @BindView(R.id.tv_item_time)
        TextView tvTime;

        DemoItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(DemoItemHolder.this, itemView);
        }
    }
}
