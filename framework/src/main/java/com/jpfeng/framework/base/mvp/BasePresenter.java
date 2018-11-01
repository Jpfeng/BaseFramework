package com.jpfeng.framework.base.mvp;

import android.app.Activity;
import android.os.Bundle;

import com.jpfeng.framework.data.model.BaseModel;
import com.jpfeng.framework.data.model.IModelCallback;
import com.jpfeng.framework.data.net.util.ErrorParser;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/4/26
 */
public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    /**
     * View 层对象
     */
    protected V mView;
    /**
     * 统一订阅关系管理
     */
    private CompositeDisposable mCompositeDisposable;

    /**
     * 将 View 层与 Presenter 层关联
     */
    @Override
    public final void attachView(V view) {
        mView = view;
        onAttach();
    }

    /**
     * 解除 View 层与 Presenter 层的关联
     */
    @Override
    public void detachView() {
        onDetach();
        if (null != mCompositeDisposable) {
            mCompositeDisposable.clear();
        }
    }

    /**
     * 进行 http 网络请求的方法。配合 {@link BaseModel#request(Flowable, FlowableTransformer, Function,
     * IModelCallback, ErrorParser)} 使用。
     * 可以对页面的订阅进行统一管理。
     *
     * @param subscriber {@link BaseModel#request(Flowable, FlowableTransformer, Function,
     *                   IModelCallback, ErrorParser)} 返回的订阅
     * @param <T>        返回数据的泛型
     */
    protected <T> void makeRequest(ResourceSubscriber<T> subscriber) {
        if (null == mCompositeDisposable) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscriber);
    }

    /**
     * View 层与 Presenter 层关联时调用。
     * 调用生命周期为 {@link Activity#onCreate(Bundle)} 或 {@link Fragment#onCreate(Bundle)}
     * 此时视图尚未初始化，建议进行变量初始化和参数获取等操作。
     */
    protected void onAttach() {
    }

    /**
     * 解除 View 层与 Presenter 层关联时调用。
     * 调用生命周期为 {@link Activity#onDestroy()} 或 {@link Fragment#onDestroy()}
     * 建议进行销毁变量等操作。
     */
    protected void onDetach() {
    }

    /**
     * 懒加载。
     * 调用时机为第一次执行 {@link Activity#onResume()} 或 {@link Fragment} 第一次可见时
     * 建议进行初始网络加载等。
     */
    @Override
    public void onLazyLoad() {
    }

    /**
     * 开始。
     * 调用生命周期为 {@link Activity#onStart()} 或 {@link Fragment#onStart()}
     * 此时视图已经初始化完毕，可以操作视图。
     */
    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
    }

    /**
     * 与 Activity 或 Fragment 生命周期对应。
     */
    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
    }

    /**
     * 与 Activity 或 Fragment 生命周期对应。
     */
    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
    }

    /**
     * 与 Activity 或 Fragment 生命周期对应。
     */
    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
    }
}
