package com.mix.framework.base.mvp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.mix.framework.base.BaseFragment;
import com.mix.framework.util.ApplicationUtils;


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
        mPresenter.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    protected void onLazyLoad() {
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
