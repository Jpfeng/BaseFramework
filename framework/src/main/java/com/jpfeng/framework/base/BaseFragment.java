package com.jpfeng.framework.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Author: Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2018/5/18
 */
public abstract class BaseFragment extends Fragment implements LifecycleObserver {

    private FragmentVisibleDelegate mVisibleDelegate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getVisibleDelegate().onCreate(savedInstanceState);
        getLifecycle().addObserver(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getVisibleDelegate().onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getPageLayoutId(), container, false);
    }

    @Override
    @CallSuper
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(getView());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getVisibleDelegate().onActivityCreated();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resumeDelegate() {
        getVisibleDelegate().onResume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pauseDelegate() {
        getVisibleDelegate().onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getVisibleDelegate().onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        getVisibleDelegate().setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getVisibleDelegate().onHiddenChanged(hidden);
    }

    /**
     * 懒加载方法
     * 首次 {@link #onResume()} 后或首次可见时执行
     */
    public void onLazyLoad() {
    }

    /**
     * 当前 fragment 可见时调用
     */
    public void onVisible() {
    }

    /**
     * 当前 fragment 不可见时调用
     */
    public void onInvisible() {
    }

    FragmentVisibleDelegate getVisibleDelegate() {
        if (mVisibleDelegate == null) {
            mVisibleDelegate = new FragmentVisibleDelegate(this);
        }
        return mVisibleDelegate;
    }

    /**
     * 寻找视图对象的方法
     *
     * @param id  视图 id
     * @param <T> 返回的类型
     * @return 该 id 对应的视图对象。 null - 未找到
     */
    @Nullable
    protected final <T extends View> T findViewById(@IdRes int id) {
        View view = getView();
        return null == view ? null : view.findViewById(id);
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
    protected abstract void init(View pageView);
}
