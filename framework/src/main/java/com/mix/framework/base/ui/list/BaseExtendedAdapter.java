package com.mix.framework.base.ui.list;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mix.framework.R;
import com.mix.framework.base.BaseApplication;
import com.mix.framework.base.ui.BaseListAdapter;
import com.mix.framework.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/10
 */
public abstract class BaseExtendedAdapter<T> extends BaseListAdapter<T> {

    private static final int DEFAULT_START_REFRESH_HEIGHT = UIUtils.dp2px(48);

    private static final int TYPE_HEADER = 199;
    private static final int TYPE_FOOTER = 198;
    private static final int TYPE_REFRESH_VIEW = 197;
    private static final int TYPE_LOAD_MORE_VIEW = 196;

    private static final int STATE_LOAD_MORE_NORMAL = 0;
    private static final int STATE_LOAD_MORE_LOADING = 1;
    private static final int STATE_LOAD_MORE_ERROR = 2;
    private static final int STATE_LOAD_MORE_NO_MORE = 3;

    private List<View> mHeaders;
    private List<View> mFooters;
    private RefreshView mRefreshView;
    private LoadMoreView mLoadMoreView;

    private OnScrollListener mScrollListener;
    private OnLoadMoreListener mLoadMoreListener;
    private OnRefreshListener mRefreshListener;

    private boolean mRefreshEnable;
    private boolean mLoadMoreEnable;
    private boolean mIsRefreshing;
    private int mLoadMoreState;
    private boolean mIsAnimating;

    private boolean mIsTop;
    private int mScrollOffset;
    private float mPullStartY;
    private boolean mPullToRefresh;
    private int mStartRefreshHeight;

    public BaseExtendedAdapter() {
        mHeaders = new ArrayList<>();
        mFooters = new ArrayList<>();
        mRefreshEnable = false;
        mLoadMoreEnable = false;
        mIsRefreshing = false;
        mLoadMoreState = STATE_LOAD_MORE_NORMAL;
        mIsAnimating = false;
        mIsTop = true;
        mScrollOffset = 0;
        mPullStartY = -1f;
        mPullToRefresh = false;
    }

    @NonNull
    @Override
    public final ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
            case TYPE_FOOTER:
            case TYPE_REFRESH_VIEW:
            case TYPE_LOAD_MORE_VIEW:
                return new HeaderFooterViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_extended_header_footer, parent, false));
            default:
                // 其他处理具体由子类实现
                return onCreateViewHolderCompat(parent, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List payloads) {
        if (isRefreshingView(position) || isHeader(position)
                || isFooter(position) || isLoadingMoreView(position)) {
            onBindViewHolder(holder, position);
        } else {
            onBindViewHolderCompat(holder, getDataPosition(position), payloads);
        }
    }

    @Override
    public final void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof HeaderFooterViewHolder) {
            final HeaderFooterViewHolder vh = (HeaderFooterViewHolder) holder;
            switch (getItemViewType(position)) {
                case TYPE_HEADER:
                    // 处理头部
                    vh.mFlRoot.addView(mHeaders.get(getHeaderPosition(position)));
                    break;
                case TYPE_FOOTER:
                    // 处理尾部
                    vh.mFlRoot.addView(mFooters.get(getFooterPosition(position)));
                    break;
                case TYPE_REFRESH_VIEW:
                    vh.mFlRoot.addView(mRefreshView);
                    // 如果不是刷新中或动画中，将高度设置为 0
                    if (!mIsRefreshing && !mIsAnimating) {
                        mRefreshView.getLayoutParams().height = 0;
                    }
                    break;
                case TYPE_LOAD_MORE_VIEW:
                    vh.mFlRoot.addView(mLoadMoreView);
                    break;
            }
        } else {
            // 其他处理具体由子类实现
            onBindViewHolderCompat(holder, getDataPosition(position));
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        if (holder instanceof HeaderFooterViewHolder) {
            // 视图在被回收后要从父控件中移除，复用时才不会出现问题。
            final HeaderFooterViewHolder vh = (HeaderFooterViewHolder) holder;
            vh.mFlRoot.removeAllViews();
        }
    }

    @Override
    public final int getItemViewType(int position) {
        if (isRefreshingView(position)) {
            return TYPE_REFRESH_VIEW;
        } else if (isHeader(position)) {
            return TYPE_HEADER;
        } else if (isFooter(position)) {
            return TYPE_FOOTER;
        } else if (isLoadingMoreView(position)) {
            return TYPE_LOAD_MORE_VIEW;
        } else {
            // 其余情况为数据条目，先判断返回值是否重复，重复则抛出异常。
            int type = getItemViewTypeCompat(getDataPosition(position));
            if (TYPE_HEADER >= type && TYPE_LOAD_MORE_VIEW <= type) {
                throw new IllegalArgumentException("you CAN NOT use 196 - 199 for item view type! try another int!");
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
    public abstract ViewHolder onCreateViewHolderCompat(@NonNull ViewGroup parent, int viewType);

    /**
     * 替代 onBindViewHolder，此方法仅会处理数据条目（非头部和加载更多视图）。
     *
     * @param holder       viewHolder
     * @param dataPosition 条目在数据集中的位置
     */
    public abstract void onBindViewHolderCompat(@NonNull ViewHolder holder, int dataPosition);

    /**
     * 替代 onBindViewHolder
     *
     * @param holder       viewHolder
     * @param dataPosition 条目在数据集中的位置
     * @param payloads     payload
     */
    public void onBindViewHolderCompat(@NonNull ViewHolder holder, int dataPosition, @NonNull List payloads) {
        onBindViewHolderCompat(holder, dataPosition);
    }

    /**
     * 返回条目类型，仅限数据条目（非头部和加载更多视图）。不可用 196 至 199 作为返回值，会抛出异常。
     *
     * @param dataPosition 条目在数据集中的位置
     * @return 类型
     */
    public int getItemViewTypeCompat(int dataPosition) {
        return 0;
    }

    @Override
    public final int getItemCount() {
        int refreshViewCount = mRefreshEnable ? 1 : 0;
        int headerCount = mHeaders.size();
        int contentCount = mData.size();
        int footerCount = mFooters.size();
        int loadingViewCount = mLoadMoreEnable ? 1 : 0;
        return refreshViewCount + headerCount + contentCount + footerCount + loadingViewCount;
    }

    /**
     * 输入一个位置，判断是否为刷新视图。
     *
     * @param position 条目在整体列表中的位置
     * @return true 是刷新视图
     */
    public boolean isRefreshingView(int position) {
        return mRefreshEnable && 0 == position;
    }

    /**
     * 输入一个位置，判断是否为头部视图。
     *
     * @param position 条目在整体列表中的位置
     * @return true 是头部视图
     */
    public boolean isHeader(int position) {
        if (mRefreshEnable) {
            return position < mHeaders.size() + 1 && 0 != position;
        } else {
            return position < mHeaders.size();
        }
    }

    /**
     * 输入一个位置，判断是否为尾部视图。
     *
     * @param position 条目在整体列表中的位置
     * @return true 是尾部视图
     */
    public boolean isFooter(int position) {
        final int itemCount = getItemCount();
        if (mLoadMoreEnable) {
            return itemCount - 2 - position < mFooters.size() && itemCount - 1 != position;
        } else {
            return itemCount - 1 - position < mFooters.size();
        }
    }

    /**
     * 输入一个位置，判断是否为加载更多视图。
     *
     * @param position 条目在整体列表中的位置
     * @return true 是加载更多视图
     */
    public boolean isLoadingMoreView(int position) {
        return mLoadMoreEnable && getItemCount() - 1 == position;
    }

    private int getHeaderPosition(int position) {
        return isHeader(position)
                ? position - (mRefreshEnable ? 1 : 0)
                : -1;
    }

    private int getFooterPosition(int position) {
        return isFooter(position)
                ? position - (mRefreshEnable ? 1 : 0) - mHeaders.size() - mData.size()
                : -1;
    }

    private int getDataPosition(int position) {
        if (isRefreshingView(position) || isHeader(position)
                || isFooter(position) || isLoadingMoreView(position)) {
            return -1;
        }
        return position - mHeaders.size() - (mRefreshEnable ? 1 : 0);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mScrollListener = new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLoadMoreEnable) {
                    int lastVisibleItemPosition = findLastVisibleItemPosition(recyclerView.getLayoutManager());
                    if (lastVisibleItemPosition == getItemCount() - 1 && STATE_LOAD_MORE_NORMAL == mLoadMoreState) {
                        loadMore();
                    }
                }
                // 记录当前的滑动距离
                mScrollOffset += dy;
                if (mScrollOffset < 0) {
                    mScrollOffset = 0;
                }
                mIsTop = 0 == mScrollOffset;
            }
        };
        recyclerView.addOnScrollListener(mScrollListener);
        recyclerView.setOnTouchListener((v, event) -> {
            if (!mRefreshEnable || mIsRefreshing || mIsAnimating) {
                // 如果未开启下拉刷新，或正在刷新，或正在进行动画，就不做处理
                return false;
            }
            boolean handled = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    // -1f == mPullStartY 表示当前没有记录开始下拉的位置，不需要处理下拉刷新
                    if (mIsTop && -1f == mPullStartY) {
                        // 如果在列表顶部了但是还未开始处理下拉刷新，则开始记录位置
                        mPullStartY = event.getY();
                    } else if (!mIsTop && -1f != mPullStartY) {
                        // 如果不在顶部了，清除开始下拉的位置
                        mPullStartY = -1f;
                    }
                    // 计算下拉的高度。如果没有记录开始下拉的位置，那高度就是 0
                    int height = (int) (-1 == mPullStartY ? 0 : event.getY() - mPullStartY) / 3;
                    if (!mPullToRefresh) {
                        // 如果当前不是下拉刷新状态，则判断是否开启下拉刷新。如果下拉的高度大于 0
                        mPullToRefresh = height > 0;
                    }
                    if (mPullToRefresh) {
                        // 处理下拉刷新
                        if (height >= 0) {
                            // 有下拉高度时，将下拉刷新视图高度进行调整
                            mRefreshView.getLayoutParams().height = height;
                            if (height >= mStartRefreshHeight) {
                                mRefreshView.pulling(1);
                            } else {
                                mRefreshView.pulling((float) height / mStartRefreshHeight);
                            }
                        } else {
                            // 如果下拉高度变为负数，则停止下拉刷新
                            mRefreshView.getLayoutParams().height = 0;
                            mRefreshView.pulling(0);
                            mPullToRefresh = false;
                        }
                        mRefreshView.requestLayout();
                        // 将返回值置为 true ，可以拦截列表自身的触摸事件处理
                        handled = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mPullToRefresh) {
                        // 如果还在下拉状态中。。
                        height = mRefreshView.getLayoutParams().height;
                        if (height >= mStartRefreshHeight) {
                            startRefreshViewAnimation(height, mStartRefreshHeight, true);
                        } else {
                            startRefreshViewAnimation(height, 0, false);
                        }
                        mPullStartY = -1;
                        mPullToRefresh = false;
                        handled = true;
                    }
                    break;
            }
            return handled;
        });
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        // 移除注册的监听器
        recyclerView.removeOnScrollListener(mScrollListener);
    }

    private void startRefreshViewAnimation(int startHeight, int targetHeight, boolean callRefresh) {
        ValueAnimator animator = ValueAnimator.ofInt(startHeight, targetHeight);
        animator.addUpdateListener(animation -> {
            mRefreshView.getLayoutParams().height = (int) (Integer) animation.getAnimatedValue();
            mRefreshView.requestLayout();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsAnimating = false;
                if (callRefresh) {
                    mIsRefreshing = true;
                    mRefreshView.startRefresh();
                    if (null != mRefreshListener) {
                        mRefreshListener.onRefresh();
                    }
                }
            }
        });
        animator.start();
    }

    /**
     * 重新设置数据。会首先将数据集清空。
     *
     * @param newData 新的数据
     */
    @Override
    public void setNewData(@Nullable List<T> newData) {
        mLoadMoreState = STATE_LOAD_MORE_NORMAL;
        super.setNewData(newData);
    }

    /**
     * 添加数据。指定数据会添加到数据集的末尾。
     *
     * @param data 要添加的数据
     */
    public void addData(@Nullable List<T> data) {
        if (null != data) {
            int start = mHeaders.size() + mData.size() + (mRefreshEnable ? 1 : 0);
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
        notifyItemInserted(mRefreshEnable ? 1 : 0);
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
            notifyItemRemoved(index + (mRefreshEnable ? 1 : 0));
        }
    }

    /**
     * 添加 Footer 。添加位置为当前列表的最底部。
     *
     * @param footerView 要添加的 View
     */
    public void addFooter(@Nullable View footerView) {
        if (null == footerView) {
            return;
        }
        mFooters.add(footerView);
        notifyItemInserted(getItemCount() - (mLoadMoreEnable ? 2 : 1));
    }

    /**
     * 移除指定的 Footer
     *
     * @param footerView 要移除的 View
     */
    public void removeFooter(@Nullable View footerView) {
        if (null == footerView) {
            return;
        }
        int index = mFooters.indexOf(footerView);
        if (0 < index) {
            mFooters.remove(footerView);
            notifyItemRemoved(
                    getItemCount() - (mLoadMoreEnable ? 1 : 0) - (mFooters.size() - index));
        }
    }

    /**
     * 启用加载更多
     *
     * @param loadMore true 为启用
     */
    public void setLoadMoreEnable(boolean loadMore) {
        mLoadMoreState = STATE_LOAD_MORE_NORMAL;
        mLoadMoreEnable = loadMore;
        if (mLoadMoreEnable) {
            if (null == mLoadMoreView) {
                setLoadMoreView(new DefaultLoadMoreView(BaseApplication.getContext()));
            }
        } else {
            mLoadMoreView = null;
        }
    }

    /**
     * 设置自定义的加载更多视图。加载后默认显示普通状态。
     *
     * @param view 加载更多视图
     */
    public void setLoadMoreView(@NonNull LoadMoreView view) {
        mLoadMoreView = view;
        mLoadMoreView.setOnClickListener((v) -> {
            if (STATE_LOAD_MORE_ERROR == mLoadMoreState) {
                loadMore();
            }
        });
        mLoadMoreView.showNormal();
    }

    /**
     * 启用下拉刷新
     *
     * @param refresh true 为启用
     */
    public void setRefreshEnable(boolean refresh) {
        mIsRefreshing = false;
        mRefreshEnable = refresh;
        if (mRefreshEnable) {
            if (null == mRefreshView) {
                setRefreshView(new DefaultRefreshView(BaseApplication.getContext()), DEFAULT_START_REFRESH_HEIGHT);
            }
        } else {
            mRefreshView = null;
        }
    }

    /**
     * 设置自定义的下拉刷新视图。
     *
     * @param view 下拉刷新视图
     */
    public void setRefreshView(@NonNull RefreshView view, @Px int startRefreshHeight) {
        mRefreshView = view;
        mStartRefreshHeight = startRefreshHeight;
    }

    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions =
                    ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(null);
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
        mLoadMoreState = STATE_LOAD_MORE_LOADING;
        if (mLoadMoreEnable) {
            if (null != mLoadMoreListener) {
                mLoadMoreListener.onLoadMore();
            }
            mLoadMoreView.showLoading();
        }
    }

    /**
     * 设置加载更多状态为加载完成
     */
    public void loadMoreEnd() {
        mLoadMoreState = STATE_LOAD_MORE_NORMAL;
        if (mLoadMoreEnable) {
            mLoadMoreView.showNormal();
        }
    }

    /**
     * 设置加载更多状态为加载错误
     */
    public void loadMoreError(String errorMessage) {
        mLoadMoreState = STATE_LOAD_MORE_ERROR;
        if (mLoadMoreEnable) {
            mLoadMoreView.showError(errorMessage);
        }
    }

    /**
     * 设置加载更多状态为没有更多
     */
    public void noMore() {
        mLoadMoreState = STATE_LOAD_MORE_NO_MORE;
        if (mLoadMoreEnable) {
            mLoadMoreView.showNoMore();
        }
    }

    private int mLastAnimationValue;

    /**
     * 刷新完成
     */
    public void refreshEnd() {
        ViewGroup.LayoutParams layoutParams = mRefreshView.getLayoutParams();
        if (null == layoutParams) {
            // 如果为空，则可能没有显示
            mIsRefreshing = false;
            return;
        }
        int startHeight = layoutParams.height;
        ValueAnimator animator = ValueAnimator.ofInt(startHeight, 0);
        animator.addUpdateListener(animation -> {
            int newHeight = (Integer) animation.getAnimatedValue();
            mRefreshView.getLayoutParams().height = newHeight;
            mRefreshView.requestLayout();
            mScrollOffset -= mLastAnimationValue - newHeight;
            if (mScrollOffset < 0) {
                mScrollOffset = 0;
            }
            mLastAnimationValue = newHeight;
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mIsAnimating = true;
                mIsRefreshing = false;
                mLastAnimationValue = startHeight;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsAnimating = false;
                mRefreshView.stopRefresh();
            }
        });
        animator.start();
    }

    /**
     * 当前是否正在刷新
     *
     * @return true 正在刷新
     */
    public boolean isRefreshing() {
        return mIsRefreshing;
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

    /**
     * 设置刷新的监听器
     *
     * @param refreshListener 监听器
     */
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        mRefreshListener = refreshListener;
    }

    public interface OnRefreshListener {
        /**
         * 刷新时调用
         */
        void onRefresh();
    }
}
