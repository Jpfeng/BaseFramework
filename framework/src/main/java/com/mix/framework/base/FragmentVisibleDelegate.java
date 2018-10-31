package com.mix.framework.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * Created by YoKey on 17/4/4.
 * from Fragmentation, mod by Jpfeng
 */
class FragmentVisibleDelegate {
    private static final String STATE_IS_INVISIBLE_WHEN_LEAVE = "invisible_when_leave";
    private static final String STATE_COMPAT_REPLACE = "compat_replace";

    // 可见性标识
    private boolean mIsVisible;
    // 执行到 onPause() 时的可见性
    private boolean mInvisibleWhenLeave;
    // 首次可见
    private boolean mIsFirstVisible = true;
    // 首次初始化的标记
    private boolean mFirstCreateViewCompatReplace = true;

    private Handler mHandler;
    private BaseFragment mFragment;

    FragmentVisibleDelegate(BaseFragment fragment) {
        this.mFragment = fragment;
    }

    void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // setUserVisibleHint() may be called before onCreate()
            mInvisibleWhenLeave = savedInstanceState.getBoolean(STATE_IS_INVISIBLE_WHEN_LEAVE);
            mFirstCreateViewCompatReplace = savedInstanceState.getBoolean(STATE_COMPAT_REPLACE);
        }
    }

    void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_IS_INVISIBLE_WHEN_LEAVE, mInvisibleWhenLeave);
        outState.putBoolean(STATE_COMPAT_REPLACE, mFirstCreateViewCompatReplace);
    }

    void onActivityCreated() {
        // 不是第一次执行 且 在 view pager 中
        // 根据 tag 判断是否在 view pager 中。参考 FragmentPagerAdapter.makeFragmentName()
        if (!mFirstCreateViewCompatReplace
                && mFragment.getTag() != null
                && mFragment.getTag().startsWith("android:switcher:")) {
            return;
        }

        // 第一次执行，或者不在 view pager 中。

        // 如果是第一次执行，清除该标记
        if (mFirstCreateViewCompatReplace) {
            mFirstCreateViewCompatReplace = false;
        }

        /* mInvisibleWhenLeave: 如果是第一次执行，则为默认值 false
         *                      如果不是第一次执行，那么就是在 view pager 中。
         *                      则值为 false 说明在之前被移除时为不显示，值为 true 说明是显示的。
         * Fragment.getUserVisibleHint() 如果不在 view pager 中永远返回 true
         * 此处判断 不显示 → 显示
         */
        if (!mInvisibleWhenLeave && !mFragment.isHidden() && mFragment.getUserVisibleHint()) {
            // 有外层 fragment 且是可见的 或 外层是 activity
            if ((mFragment.getParentFragment() != null && isFragmentVisible(mFragment.getParentFragment()))
                    || mFragment.getParentFragment() == null) {
                // 改变可见性标识但不向内层分发事件
                changeVisibility(true, false);
            }
        }
    }

    void onResume() {
        // 非首次可见
        if (!mIsFirstVisible) {
            // 判断之前不可见当前可见
            if (!mIsVisible && !mInvisibleWhenLeave && isFragmentVisible(mFragment)) {
                // 改变可见性标识但不向内层分发事件
                changeVisibility(true, false);
            }
        }
    }

    void onPause() {
        // 当前可见
        if (mIsVisible && isFragmentVisible(mFragment)) {
            // 改变可见性标识但不向内层分发事件
            mInvisibleWhenLeave = false;
            changeVisibility(false, false);
        } else {
            mInvisibleWhenLeave = true;
        }
    }

    void onHiddenChanged(boolean hidden) {
        if (!hidden && !mFragment.isResumed()) {
            // if fragment is shown but not resumed, ignore...
            mInvisibleWhenLeave = false;
            return;
        }
        // 分发事件
        if (hidden) {
            safeChangeVisibilityAndDispatch(false);
        } else {
            enqueueDispatchVisible(true);
        }
    }

    void onDestroyView() {
        mIsFirstVisible = true;
    }

    void setUserVisibleHint(boolean isVisibleToUser) {
        if (mFragment.isResumed() || (!mFragment.isAdded() && isVisibleToUser)) {
            if (!mIsVisible && isVisibleToUser) {
                safeChangeVisibilityAndDispatch(true);
            } else if (mIsVisible && !isVisibleToUser) {
                changeVisibility(false, true);
            }
        }
    }

    private void safeChangeVisibilityAndDispatch(boolean visible) {
        if (mIsFirstVisible) {
            if (!visible) {
                return;
            }
            enqueueDispatchVisible(true);
        } else {
            changeVisibility(visible, true);
        }
    }

    private void enqueueDispatchVisible(boolean needDispatch) {
        getHandler().post(() -> changeVisibility(true, needDispatch));
    }

    private void changeVisibility(boolean visible, boolean needDispatch) {
        // 如果父不可见，忽略可见事件
        // 如果可见性未改变，忽略
        // 如果未添加或已经移除，忽略
        if ((visible && isParentInvisible())
                || mIsVisible == visible
                || !mFragment.isAdded()) {
            return;
        }

        // 改变可见性标识
        mIsVisible = visible;

        // 执行回调
        // 根据需要分发可见性改变
        if (visible) {
            mFragment.onVisible();
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                mFragment.onLazyLoad();
            }
            if (needDispatch) {
                dispatchVisibility(true);
            }

        } else {
            if (needDispatch) {
                dispatchVisibility(false);
            }
            mFragment.onInvisible();
        }
    }

    private void dispatchVisibility(boolean visible) {
        // 获取全部子 fragment 并分发可见性改变事件
        List<Fragment> childFragments = mFragment.getChildFragmentManager().getFragments();
        for (Fragment child : childFragments) {
            if (child instanceof BaseFragment && !child.isHidden() && child.getUserVisibleHint()) {
                ((BaseFragment) child).getVisibleDelegate().changeVisibility(visible, true);
            }
        }
    }

    private boolean isParentInvisible() {
        BaseFragment fragment = (BaseFragment) mFragment.getParentFragment();
        return fragment != null && !fragment.getVisibleDelegate().isVisible();
    }

    private boolean isFragmentVisible(Fragment fragment) {
        return !fragment.isHidden() && fragment.getUserVisibleHint();
    }

    private boolean isVisible() {
        return mIsVisible;
    }

    private Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }
}
