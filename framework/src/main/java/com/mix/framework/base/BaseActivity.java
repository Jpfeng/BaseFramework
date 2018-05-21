package com.mix.framework.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/18
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * onLazyLoad() 方法是否已经调用
     */
    private boolean mLazyLoadCalled;

    public BaseActivity() {
        mLazyLoadCalled = false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getPageView());
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mLazyLoadCalled) {
            onLazyLoad();
            mLazyLoadCalled = true;
        }
    }

    /**
     * 懒加载方法。首次 onResume() 时执行。
     */
    protected void onLazyLoad() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLazyLoadCalled = false;
    }

    /**
     * 返回页面内容的布局
     *
     * @return 页面内容布局
     */
    @LayoutRes
    protected abstract int getPageView();

    /**
     * 初始化页面。
     */
    protected abstract void init();
}
