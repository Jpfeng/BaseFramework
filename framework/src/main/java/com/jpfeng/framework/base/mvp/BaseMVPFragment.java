package com.jpfeng.framework.base.mvp;

import android.os.Bundle;
import android.view.View;

import com.jpfeng.framework.base.BaseFragment;
import com.jpfeng.framework.util.ApplicationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/14
 */
public abstract class BaseMVPFragment<P extends IBasePresenter> extends BaseFragment implements IBaseView {

    /**
     * Presenter 层对象
     */
    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(createPresenter());
    }

    @Override
    protected void init(View pageView) {
        initPage(pageView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(mPresenter);
    }

    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
        mPresenter.onLazyLoad();
    }

    /**
     * 设置 Presenter 对象
     *
     * @param presenter presenter 对象
     */
    protected void setPresenter(P presenter) {
        mPresenter = ApplicationUtils.checkNonNull(presenter);
        getLifecycle().addObserver(mPresenter);
        mPresenter.attachView(this);
    }

    /**
     * 创建 Presenter 对象，此处禁止返回 null 。
     *
     * @return Presenter 对象
     */
    @NonNull
    protected abstract P createPresenter();

    /**
     * 初始化页面。此时 mContentView 和 mPresenter 均已可用。
     */
    protected abstract void initPage(View pageView);
}
