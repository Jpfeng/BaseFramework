package com.jpfeng.framework.base.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jpfeng.framework.R;
import com.jpfeng.framework.base.mvp.BaseMVPFragment;
import com.jpfeng.framework.base.mvp.IBasePresenter;

import androidx.annotation.LayoutRes;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/4/27
 */
public abstract class BaseStateFragment<P extends IBasePresenter> extends BaseMVPFragment<P> implements IStateView {

    /**
     * ButterKnife 的 Unbinder 对象
     */
    private Unbinder mUnbinder;

    /**
     * 页面根视图，四种状态视图的父视图
     */
    private FrameLayout mFlRoot;
    /**
     * 空白页面视图
     */
    private View mEmptyView;
    /**
     * 错误页面视图
     */
    private View mErrorView;
    /**
     * 加载状态视图
     */
    private View mLoadingView;
    /**
     * 页面内容视图
     */
    private View mContentView;

    @Override
    protected void initPage(View pageView) {
        mFlRoot = pageView.findViewById(R.id.fl_fragment_base_root);
        mContentView = LayoutInflater.from(pageView.getContext()).inflate(getContentView(), mFlRoot, false);
        // 默认所有状态视图初始均为 View.GONE ，在需要显示的时候请手动进行调用。
        mContentView.setVisibility(View.GONE);
        mFlRoot.addView(mContentView);
        // 集成 ButterKnife 在具体页面可直接添加注解。
        mUnbinder = ButterKnife.bind(this, mContentView);
        initContent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mEmptyView = null;
        mErrorView = null;
        mLoadingView = null;
    }

    @Override
    protected int getPageLayoutId() {
        return R.layout.fragment_base;
    }

    /**
     * 设置自定义的空白视图。建议在 initContent() 方法中使用。
     * 可以在调用后通过 mEmptyView.findViewById(int) 方法获取视图内的对象。
     *
     * @param layoutID 自定义空白视图的布局 ID
     */
    protected void setCustomEmptyView(@LayoutRes int layoutID) {
        if (null != mEmptyView) {
            mFlRoot.removeView(mEmptyView);
        }
        mEmptyView = LayoutInflater.from(getContext()).inflate(layoutID, mFlRoot, false);
        mEmptyView.setVisibility(View.GONE);
        mFlRoot.addView(mEmptyView);
    }

    /**
     * 设置自定义的错误视图。建议在 initContent() 方法中使用。
     * 可以在调用后通过 mErrorView.findViewById(int) 方法获取视图内的对象。
     *
     * @param layoutID 自定义错误视图的布局 ID
     */
    protected void setCustomErrorView(@LayoutRes int layoutID) {
        if (null != mErrorView) {
            mFlRoot.removeView(mErrorView);
        }
        mErrorView = LayoutInflater.from(getContext()).inflate(layoutID, mFlRoot, false);
        mErrorView.setVisibility(View.GONE);
        mFlRoot.addView(mErrorView);
    }

    /**
     * 设置自定义的加载视图。建议在 initContent() 方法中使用。
     * 可以在调用后通过 mLoadingView.findViewById(int) 方法获取视图内的对象。
     *
     * @param layoutID 自定义加载视图的布局 ID
     */
    protected void setCustomLoadingView(@LayoutRes int layoutID) {
        if (null != mLoadingView) {
            mFlRoot.removeView(mLoadingView);
        }
        mLoadingView = LayoutInflater.from(getContext()).inflate(layoutID, mFlRoot, false);
        mLoadingView.setVisibility(View.GONE);
        mFlRoot.addView(mLoadingView);
    }

    /**
     * 显示空白页面，并隐藏其他
     */
    @Override
    public void showPageEmpty() {
        if (null == mEmptyView) {
            mEmptyView = LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_page_empty_default, mFlRoot, false);
            mFlRoot.addView(mEmptyView);
        }
        hideAllStateExcept(mEmptyView);
    }

    /**
     * 显示错误页面，并隐藏其他
     */
    @Override
    public void showPageError(String msg) {
        if (null == mErrorView) {
            mErrorView = LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_page_error_default, mFlRoot, false);
            TextView tvHint = mErrorView.findViewById(R.id.tv_page_error_hint);
            tvHint.setText(msg);
            tvHint.setOnClickListener(v -> onErrorViewClicked());
            mFlRoot.addView(mErrorView);
        }
        hideAllStateExcept(mErrorView);
    }

    /**
     * 显示加载页面，并隐藏其他
     */
    @Override
    public void showPageLoading() {
        if (null == mLoadingView) {
            mLoadingView = LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_page_loading_default, mFlRoot, false);
            mFlRoot.addView(mLoadingView);
        }
        hideAllStateExcept(mLoadingView);
    }

    /**
     * 显示页面内容，并隐藏其他
     */
    @Override
    public void showPageContent() {
        hideAllStateExcept(mContentView);
    }

    private void hideAllStateExcept(View exception) {
        final int count = mFlRoot.getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = mFlRoot.getChildAt(i);
            child.setVisibility(child == exception ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 点击错误视图时进行的操作。可在子类重写。
     */
    protected void onErrorViewClicked() {
    }

    /**
     * 返回页面内容的布局
     *
     * @return 页面内容布局
     */
    @LayoutRes
    protected abstract int getContentView();

    /**
     * 初始化页面。此时 mContentView 和 mPresenter 均已可用。
     */
    protected abstract void initContent();
}
