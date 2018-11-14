package com.jpfeng.framework.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jpfeng.framework.R;

import androidx.annotation.AttrRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

/**
 * 可显示状态的视图容器
 *
 * @author Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2018/11/1
 * @attr ref R.styleable#StateContainerView_sc_state
 * @attr ref R.styleable#StateContainerView_sc_emptyLayout
 * @attr ref R.styleable#StateContainerView_sc_errorLayout
 * @attr ref R.styleable#StateContainerView_sc_loadingLayout
 */
public class StateContainerView extends FrameLayout {

    public static final int STATE_CONTENT = 0;
    public static final int STATE_EMPTY = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_LOADING = 3;
    private static final int TOTAL_STATE_COUNT = 4;

    /**
     * 各状态视图数组
     */
    private final View[] mStateViews = new View[TOTAL_STATE_COUNT];
    private int mCurrentState;

    public StateContainerView(@NonNull Context context) {
        this(context, null);
    }

    public StateContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.stateContainerViewStyle);
    }

    public StateContainerView(@NonNull Context context, @Nullable AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        retrieveStyledAttribute(context, attrs, defStyleAttr, R.style.defaultStateViewStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StateContainerView(@NonNull Context context, @Nullable AttributeSet attrs,
                              int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        retrieveStyledAttribute(context, attrs, defStyleAttr, defStyleRes);
    }

    private void retrieveStyledAttribute(@NonNull Context context, AttributeSet set,
                                         @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        TypedArray ta = context.obtainStyledAttributes(set, R.styleable.StateContainerView,
                defStyleAttr, defStyleRes);
        mCurrentState = ta.getInt(R.styleable.StateContainerView_sc_state, STATE_CONTENT);
        int emptyResourceId = ta.getResourceId(R.styleable.StateContainerView_sc_emptyLayout, 0);
        if (emptyResourceId > 0) {
            setEmptyView(LayoutInflater.from(context).inflate(emptyResourceId, null));
        }
        int errorResourceId = ta.getResourceId(R.styleable.StateContainerView_sc_errorLayout, 0);
        if (errorResourceId > 0) {
            setErrorView(LayoutInflater.from(context).inflate(errorResourceId, null));
        }
        int loadingResourceId = ta.getResourceId(R.styleable.StateContainerView_sc_loadingLayout, 0);
        if (loadingResourceId > 0) {
            setLoadingView(LayoutInflater.from(context).inflate(loadingResourceId, null));
        }
        ta.recycle();
    }

    @Override
    public void addView(View child) {
        addView(child, -1);
    }

    @Override
    public void addView(View child, int index) {
        addView(child, index, null);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        addView(child, -1, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        final ViewGroup.LayoutParams params = generateDefaultLayoutParams();
        params.width = width;
        params.height = height;
        addView(child, -1, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (hasContent()) {
            throw new IllegalStateException("StateContainerView can host only one content child");
        }
        mStateViews[STATE_CONTENT] = child;
        if (mCurrentState != STATE_CONTENT) {
            child.setVisibility(GONE);
        }
        addViewInternal(child, index, params);
    }

    private boolean hasContent() {
        return getChildCount() > 0 && containsChild(getStateView(STATE_CONTENT));
    }

    private boolean containsChild(View view) {
        return indexOfChild(view) >= 0;
    }

    private void addViewInternal(View child, int index, ViewGroup.LayoutParams params) {
        if (null == params) {
            params = child.getLayoutParams();
            if (null == params) {
                params = generateDefaultLayoutParams();
                if (null == params) {
                    throw new IllegalArgumentException("generateDefaultLayoutParams() cannot return null");
                }
            }
        }
        super.addView(child, index, params);
    }

    /**
     * 指定内容视图
     *
     * @param contentView 内容视图
     */
    public void setContentView(View contentView) {
        setStateView(contentView, STATE_CONTENT);
    }

    /**
     * 指定空视图
     *
     * @param emptyView 空视图
     */
    public void setEmptyView(View emptyView) {
        setStateView(emptyView, STATE_EMPTY);
    }

    /**
     * 指定错误视图
     *
     * @param errorView 错误视图
     */
    public void setErrorView(View errorView) {
        setStateView(errorView, STATE_ERROR);
    }

    /**
     * 指定加载视图
     *
     * @param loadingView 加载视图
     */
    public void setLoadingView(View loadingView) {
        setStateView(loadingView, STATE_LOADING);
    }

    private View getStateView(@State int state) {
        return mStateViews[state];
    }

    private void setStateView(View view, @State int state) {
        // remove current
        removeView(getStateView(mCurrentState));
        mStateViews[state] = view;
        if (mCurrentState != state) {
            view.setVisibility(GONE);
        }
        addViewInternal(view, -1, null);
    }

    /**
     * 显示内容
     */
    public void showContent() {
        showState(STATE_CONTENT);
    }

    /**
     * 显示空页面
     */
    public void showEmpty() {
        showState(STATE_EMPTY);
    }

    /**
     * 显示错误页面
     */
    public void showError() {
        showState(STATE_ERROR);
    }

    /**
     * 显示加载页面
     */
    public void showLoading() {
        showState(STATE_LOADING);
    }

    private void showState(@State int state) {
        if (mCurrentState == state) {
            return;
        }
        mCurrentState = state;
        hideAllStateExcept(getStateView(state));
    }

    private void hideAllStateExcept(View specific) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            child.setVisibility(child == specific ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 返回当前状态
     *
     * @return 当前状态
     * @see #STATE_CONTENT
     * @see #STATE_EMPTY
     * @see #STATE_ERROR
     * @see #STATE_LOADING
     */
    @State
    public int getCurrentState() {
        return mCurrentState;
    }

    @IntDef({STATE_CONTENT, STATE_EMPTY, STATE_ERROR, STATE_LOADING})
    public @interface State {
    }
}
