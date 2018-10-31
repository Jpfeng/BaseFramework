package com.mix.framework.base;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/18
 */
public abstract class BaseActivity extends AppCompatActivity implements LifecycleObserver {

    /**
     * {@link #onLazyLoad()} 方法是否已经调用
     */
    private boolean mLazyLoadCalled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetLazyLoadFlag();
        getLifecycle().addObserver(this);
        setContentView(getPageLayoutId());
        init();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void performLazyLoadIfNeed() {
        if (!mLazyLoadCalled) {
            onLazyLoad();
            mLazyLoadCalled = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(this);
        resetLazyLoadFlag();
    }

    private void resetLazyLoadFlag() {
        mLazyLoadCalled = false;
    }

    /**
     * 懒加载方法，首次 {@link #onResume()} 后执行
     */
    protected void onLazyLoad() {
    }

    /**
     * 返回页面内容的布局
     *
     * @return 页面内容布局
     */
    @LayoutRes
    protected abstract int getPageLayoutId();

    /**
     * 初始化页面
     */
    protected abstract void init();
}
