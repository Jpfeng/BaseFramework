package com.mix.framework.base.mvp;

import android.support.annotation.NonNull;

import com.mix.framework.base.BaseActivity;
import com.mix.framework.util.ApplicationUtils;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/14
 */
public abstract class BaseMVPActivity<P extends IBasePresenter> extends BaseActivity implements IBaseView {

    /**
     * Presenter 层对象
     */
    protected P mPresenter;

    @Override
    protected void init() {
        setPresenter(createPresenter());
        initPage();
        mPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
     * 初始化页面。此时视图和 mPresenter 均已可用。
     */
    protected abstract void initPage();
}
