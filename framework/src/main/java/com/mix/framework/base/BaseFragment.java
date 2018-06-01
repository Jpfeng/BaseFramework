package com.mix.framework.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/18
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 是否已经初始化完成
     */
    private boolean mInited;
    /**
     * mPresenter#onLazyLoad() 方法是否已经调用。
     */
    private boolean mLazyLoadCalled;
    /**
     * Fragment#setUserVisibleHint(boolean) 方法是否已经调用。
     */
    private boolean mSetUserVisibleHintCalled;
    /**
     * 需要调用 onLazyLoad() 方法，但是视图还未初始化。设置此标记以延迟到 onResume() 时调用。
     */
    private boolean mShouldCallLazyLoad;
    /**
     * 是否可见
     */
    private boolean mIsVisible;
    /**
     * 执行了 onStop() 但未执行 onDestroyView()
     */
    private boolean mStoppedByOthers;
    /**
     * 该 Fragment 的视图对象
     */
    private View mView;

    public BaseFragment() {
        mInited = false;
        mLazyLoadCalled = false;
        mSetUserVisibleHintCalled = false;
        mShouldCallLazyLoad = false;
        mStoppedByOthers = false;
        mIsVisible = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getPageView(), container, false);
        init(mView);
        mInited = true;
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fragment 套嵌在 Viewpager 中时，Fragment#setUserVisibleHint(boolean) 会在 Fragment#onResume() 之前调用。
        // 此处判断 (!mSetUserVisibleHintCalled && !mLazyLoadCalled) 说明此 Fragment 并非在 ViewPager 中。
        // mStoppedByOthers = true 说明之前执行过 onStop() ,可能是被其他 activity 覆盖了。此处需要执行 onVisible() 。
        if (((!mSetUserVisibleHintCalled && !mLazyLoadCalled) && !mIsVisible)
                || (mStoppedByOthers && !mIsVisible)) {
            mIsVisible = true;
            onVisible();
            mStoppedByOthers = false;
        }
        // Fragment 套嵌在 Viewpager 中时，Fragment#setUserVisibleHint(boolean) 会在 Fragment#onResume() 之前调用。
        // 此处判断 (!mSetUserVisibleHintCalled && !mLazyLoadCalled) 说明此 Fragment 并非在 ViewPager 中，在此处
        // 调用 mPresenter#onLazyLoad() 即可。
        // 判断 mShouldCallLazyLoad 说明此 Fragment 在 ViewPager 中，但是在 Fragment#setUserVisibleHint(boolean) 时
        // 视图尚未初始化，所以延迟到此处调用。
        if ((!mSetUserVisibleHintCalled && !mLazyLoadCalled) || mShouldCallLazyLoad) {
            onLazyLoad();
            mLazyLoadCalled = true;
            mShouldCallLazyLoad = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!mSetUserVisibleHintCalled) {
            mSetUserVisibleHintCalled = true;
        }
        if (isVisibleToUser && !mIsVisible) {
            mIsVisible = true;
            onVisible();
        } else if (!isVisibleToUser && mIsVisible) {
            mIsVisible = false;
            onInvisible();
        }
        // 已经可见但是还未调用 mPresenter#onLazyLoad()。
        if (isVisibleToUser && !mLazyLoadCalled) {
            if (mInited) {
                // 若视图已经初始化，则直接调用 mPresenter#onLazyLoad()。
                onLazyLoad();
                mLazyLoadCalled = true;
            } else {
                // 若视图还未初始化，则延迟到 onResume() 调用。
                mShouldCallLazyLoad = true;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mIsVisible) {
            mIsVisible = false;
            onInvisible();
            mStoppedByOthers = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mInited = false;
        mLazyLoadCalled = false;
        mSetUserVisibleHintCalled = false;
        mShouldCallLazyLoad = false;
        mStoppedByOthers = false;
        mIsVisible = false;
    }

    /**
     * 懒加载方法。首次 onResume() 时执行。
     */
    protected void onLazyLoad() {
    }

    /**
     * 当前 Fragment 可见时调用。
     */
    protected void onVisible() {
    }

    /**
     * 当前 Fragment 不可见时调用。
     */
    protected void onInvisible() {
    }

    /**
     * 寻找视图对象的方法。
     *
     * @param id  视图 id
     * @param <T> 返回的类型
     * @return 该 id 对应的视图对象。 null - 未找到。
     */
    @Nullable
    protected final <T extends View> T findViewById(@IdRes int id) {
        if (null == mView) {
            return null;
        }
        return mView.findViewById(id);
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
    protected abstract void init(View pageView);
}
