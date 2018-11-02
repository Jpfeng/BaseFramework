package com.jpfeng.framework.base.ui;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/10
 */
public abstract class BaseListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final ItemComparator<T> mComparator;
    protected List<T> mData;

    public BaseListAdapter(ItemComparator<T> comparator) {
        mComparator = comparator;
    }

    /**
     * 重新设置数据。会自动判断数据变化。
     *
     * @param data 新的数据
     */
    public void setData(List<T> data) {
        if (null == mData) {
            mData = data;
            notifyItemRangeInserted(0, data.size());

        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mData.size();
                }

                @Override
                public int getNewListSize() {
                    return data.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mComparator.compareItem(mData.get(oldItemPosition), data.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return mComparator.compareContent(mData.get(oldItemPosition), data.get(newItemPosition));
                }
            });

            mData = data;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public int getItemCount() {
        return null == mData ? 0 : mData.size();
    }

    public interface ItemComparator<T> {
        /**
         * 判断两个对象是否相同条目。例如，你的每个条目有独特的 id 可以在此进行判断。
         *
         * @param oldItem 列表中的旧对象
         * @param newItem 列表中的新对象
         * @return true - 是相同条目 false - 不相同
         */
        boolean compareItem(T oldItem, T newItem);

        /**
         * 判断两对象内容是否相同，判断条目内容变化。
         * <p>
         * 列表使用此方法代替 {@link Object#equals(Object)} 进行判断，你可以根据你的 UI 对判断内容进行定制。
         * 例如，只判断界面可见的属性是否变化
         * <p>
         * 如果 {@link #compareItem(T, T)} 返回 true 才会调用此方法。
         *
         * @param oldItem 列表中的旧对象
         * @param newItem 列表中替换旧对象的新对象
         * @return @return true - 条目内容相同 false - 不相同
         */
        boolean compareContent(T oldItem, T newItem);
    }
}
