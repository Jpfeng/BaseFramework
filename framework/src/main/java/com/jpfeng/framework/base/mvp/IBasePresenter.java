package com.jpfeng.framework.base.mvp;

import androidx.lifecycle.LifecycleObserver;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/4/26
 */
public interface IBasePresenter<V extends IBaseView> extends LifecycleObserver {
    void attachView(V view);
    void detachView();
    void onLazyLoad();
    void onStart();
    void onResume();
    void onPause();
    void onStop();
}
