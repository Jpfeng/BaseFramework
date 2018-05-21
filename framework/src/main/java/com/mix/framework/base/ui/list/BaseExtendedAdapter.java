package com.mix.framework.base.ui.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mix.framework.R;
import com.mix.framework.base.ui.BaseListAdapter;
import com.mix.framework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/10
 */
public abstract class BaseExtendedAdapter<T> extends BaseListAdapter<T> {

    private static final int TYPE_HEADER = 199;
    private static final int TYPE_LOAD_MORE_VIEW = 198;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_LOADING = 1;
    private static final int STATE_ERROR = 2;
    private static final int STATE_NO_MORE = 3;

    private List<View> mHeaders;
    private RecyclerView mRecyclerView;

    private OnScrollListener mScrollListener;
    private OnLoadMoreListener mLoadMoreListener;

    private boolean mLoadMoreEnable;
    private boolean mNeedAddScrollListenerToRecyclerView;

    private int mLoadingMoreState;

    public BaseExtendedAdapter() {
        mHeaders = new ArrayList<>();
        mLoadMoreEnable = false;
        mNeedAddScrollListenerToRecyclerView = false;
        mLoadingMoreState = STATE_NORMAL;
    }

    @NonNull
    @Override
    public final ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                // 处理头部
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_extended_header, parent, false));
            case TYPE_LOAD_MORE_VIEW:
                // 处理加载更多
                return new LoadingMoreViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_extended_loading_more, parent, false));
            default:
                // 其他处理具体由子类实现
                return onCreateViewHolderCompact(parent, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List payloads) {
        if (isHeader(position) || isLoadingMoreView(position)) {
            onBindViewHolder(holder, position);
        } else {
            onBindViewHolderCompact(holder, getDataPosition(position), payloads);
        }
    }

    @Override
    public final void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            // 处理头部
            final HeaderViewHolder vh = (HeaderViewHolder) holder;
            vh.mFlRoot.addView(mHeaders.get(position));
        } else if (holder instanceof LoadingMoreViewHolder) {
            // 处理加载更多
            final LoadingMoreViewHolder vh = (LoadingMoreViewHolder) holder;
            switch (mLoadingMoreState) {
                case STATE_NORMAL:
                default:
                    vh.mLlRoot.setOnClickListener(null);
                    vh.mPbProgress.setVisibility(View.GONE);
                    vh.mTvHint.setText(StringUtils.getString(R.string.loading_more));
                    break;

                case STATE_LOADING:
                    vh.mLlRoot.setOnClickListener(null);
                    vh.mPbProgress.setVisibility(View.VISIBLE);
                    vh.mTvHint.setText(StringUtils.getString(R.string.loading_more_loading));
                    break;

                case STATE_ERROR:
                    vh.mLlRoot.setOnClickListener(v -> loadMore());
                    vh.mPbProgress.setVisibility(View.GONE);
                    vh.mTvHint.setText(StringUtils.getString(R.string.loading_more_error));
                    break;

                case STATE_NO_MORE:
                    vh.mLlRoot.setOnClickListener(null);
                    vh.mPbProgress.setVisibility(View.GONE);
                    vh.mTvHint.setText(StringUtils.getString(R.string.loading_more_end));
                    break;
            }
        } else {
            // 其他处理具体由子类实现
            onBindViewHolderCompact(holder, getDataPosition(position));
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        if (holder instanceof HeaderViewHolder) {
            // 头部在被回收后要从父控件中移除，复用时才不会出现问题。
            final HeaderViewHolder vh = (HeaderViewHolder) holder;
            vh.mFlRoot.removeAllViews();
        }
    }

    @Override
    public final int getItemViewType(int position) {
        if (isHeader(position)) {
            // 如果是 header ，返回 TYPE_HEADER
            return TYPE_HEADER;
        } else if (isLoadingMoreView(position)) {
            // 如果是加载更多，返回 TYPE_LOAD_MORE_VIEW
            return TYPE_LOAD_MORE_VIEW;
        } else {
            // 其余情况为数据条目，先判断返回值是否重复，重复则抛出异常。
            int type = getItemViewTypeCompact(getDataPosition(position));
            if (TYPE_HEADER == type || TYPE_LOAD_MORE_VIEW == type) {
                throw new IllegalArgumentException("you CAN NOT use 198 or 199 for item view type! try another int!");
            }
            return type;
        }
    }

    /**
     * 替代 onCreateViewHolder，此方法仅会处理数据条目（非头部和加载更多视图）。
     *
     * @param parent   父类视图
     * @param viewType 条目类型
     */
    public abstract ViewHolder onCreateViewHolderCompact(@NonNull ViewGroup parent, int viewType);

    /**
     * 替代 onBindViewHolder，此方法仅会处理数据条目（非头部和加载更多视图）。
     *
     * @param holder       viewHolder
     * @param dataPosition 条目在数据集中的位置
     */
    public abstract void onBindViewHolderCompact(@NonNull ViewHolder holder, int dataPosition);

    /**
     * 替代 onBindViewHolder
     *
     * @param holder       viewHolder
     * @param dataPosition 条目在数据集中的位置
     * @param payloads     payload
     */
    public void onBindViewHolderCompact(@NonNull ViewHolder holder, int dataPosition, @NonNull List payloads) {
        onBindViewHolderCompact(holder, dataPosition);
    }

    /**
     * 返回条目类型，仅限数据条目（非头部和加载更多视图）。不可用 198 或 199 作为返回值，会抛出异常。
     *
     * @param dataPosition 条目在数据集中的位置
     * @return 类型
     */
    public int getItemViewTypeCompact(int dataPosition) {
        return 0;
    }

    @Override
    public final int getItemCount() {
        int headerCount = mHeaders.size();
        int contentCount = mData.size();
        int footerCount = mLoadMoreEnable ? 1 : 0;
        return headerCount + contentCount + footerCount;
    }

    /**
     * 输入一个位置，判断是否为头部视图。
     *
     * @param position 条目在整体列表中的位置
     * @return true 是头部视图
     */
    public boolean isHeader(int position) {
        return position < mHeaders.size();
    }

    /**
     * 输入一个位置，判断是否为加载更多视图。
     *
     * @param position 条目在整体列表中的位置
     * @return true 是加载更多视图
     */
    public boolean isLoadingMoreView(int position) {
        return mLoadMoreEnable && position == getItemCount() - 1;
    }

    private int getDataPosition(int position) {
        if (isHeader(position) || isLoadingMoreView(position)) {
            return -1;
        }
        return position - mHeaders.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        if (mNeedAddScrollListenerToRecyclerView) {
            mRecyclerView.addOnScrollListener(mScrollListener);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (recyclerView == mRecyclerView) {
            mRecyclerView = null;
        }
    }

    /**
     * 重新设置数据。会首先将数据集清空。
     *
     * @param newData 新的数据
     */
    @Override
    public void setNewData(@Nullable List<T> newData) {
        mLoadingMoreState = STATE_NORMAL;
        super.setNewData(newData);
    }

    /**
     * 添加数据。指定数据会添加到数据集的末尾。
     *
     * @param data 要添加的数据
     */
    public void addData(@Nullable List<T> data) {
        if (null != data) {
            int start = mHeaders.size() + mData.size();
            mData.addAll(data);
            notifyItemRangeInserted(start, data.size());
        }
    }

    /**
     * 添加 Header 。添加位置为当前列表的最顶部。
     *
     * @param headerView 要添加的 View
     */
    public void addHeader(@Nullable View headerView) {
        if (null == headerView) {
            return;
        }
        mHeaders.add(0, headerView);
        notifyItemInserted(0);
    }

    /**
     * 移除指定的 Header
     *
     * @param headerView 要移除的 View
     */
    public void removeHeader(@Nullable View headerView) {
        if (null == headerView) {
            return;
        }
        int index = mHeaders.indexOf(headerView);
        if (0 < index) {
            mHeaders.remove(headerView);
            notifyItemRemoved(index);
        }
    }

    /**
     * 启用加载更多
     *
     * @param loadMore true 为启用
     */
    public void setLoadMoreEnable(boolean loadMore) {
        mLoadingMoreState = STATE_NORMAL;
        if (loadMore) {
            mLoadMoreEnable = true;
            mScrollListener = new OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    int lastVisibleItemPosition = findLastVisibleItemPosition(recyclerView.getLayoutManager());
                    if (RecyclerView.SCROLL_STATE_IDLE == newState &&
                            lastVisibleItemPosition == getItemCount() - 1 &&
                            STATE_NORMAL == mLoadingMoreState) {
                        loadMore();
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
//                    int lastVisibleItemPosition = findLastVisibleItemPosition(recyclerView.getLayoutManager());
//                    if (lastVisibleItemPosition == getItemCount() - 1 && STATE_NORMAL == mLoadingMoreState) {
//                        loadMore();
//                    }
                }
            };

            // 如果当前有 mRecyclerView 的实例，则设置滑动监听。否则设置标记延迟设置。
            if (null != mRecyclerView) {
                mRecyclerView.addOnScrollListener(mScrollListener);
            } else {
                mNeedAddScrollListenerToRecyclerView = true;
            }
        } else {
            mLoadMoreEnable = false;
            if (null != mRecyclerView) {
                mRecyclerView.removeOnScrollListener(mScrollListener);
                mScrollListener = null;
            }
        }
    }

    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            return findMax(lastVisibleItemPositions);
        }
        return -1;
    }

    private int findMax(int[] lastVisiblePositions) {
        int max = lastVisiblePositions[0];
        for (int value : lastVisiblePositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private void loadMore() {
        mLoadingMoreState = STATE_LOADING;
        if (mLoadMoreEnable) {
            notifyItemChanged(getItemCount() - 1);
            if (null != mLoadMoreListener) {
                mLoadMoreListener.onLoadMore();
            }
        }
    }

    /**
     * 设置加载更多状态为加载完成
     */
    public void loadMoreEnd() {
        mLoadingMoreState = STATE_NORMAL;
        if (mLoadMoreEnable) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    /**
     * 设置加载更多状态为加载错误
     */
    public void loadMoreError() {
        mLoadingMoreState = STATE_ERROR;
        if (mLoadMoreEnable) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    /**
     * 设置加载更多状态为没有更多
     */
    public void noMore() {
        mLoadingMoreState = STATE_NO_MORE;
        if (mLoadMoreEnable) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    /**
     * 设置加载更多的监听器
     *
     * @param loadMoreListener 监听器
     */
    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public interface OnLoadMoreListener {
        /**
         * 加载更多时调用
         */
        void onLoadMore();
    }
}
