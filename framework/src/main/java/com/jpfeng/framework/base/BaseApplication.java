package com.jpfeng.framework.base;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/8
 */
public class BaseApplication extends Application {

    private static WeakReference<Context> mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = new WeakReference<>(getApplicationContext());
    }

    /**
     * 全局获取 Context
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        return mContext.get();
    }
}
