package com.mix.framework.base;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/8
 */
public abstract class BaseApplication extends Application {

    private static WeakReference<Context> mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = new WeakReference<>(getApplicationContext());
        init();
    }

    /**
     * 全局获取 Context
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        return mContext.get();
    }

    /**
     * 对 Application 进行初始化
     */
    public abstract void init();
}
