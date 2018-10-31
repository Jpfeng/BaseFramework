package com.mix.framework.base.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/8
 */
public abstract class BaseDialogFragment extends DialogFragment {

    private Unbinder mUnbinder;
    private int mWidth = WindowManager.LayoutParams.MATCH_PARENT;
    private int mHeight = WindowManager.LayoutParams.MATCH_PARENT;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (null != window) {
            // 将对话框内部背景设为透明
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View view = inflater.inflate(getContentView(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (null != window) {
            WindowManager.LayoutParams windowParams = window.getAttributes();
            // 将对话框外部背景设为透明
            windowParams.dimAmount = 0.0f;
            // 设置对话框的宽和高
            windowParams.width = mWidth;
            windowParams.height = mHeight;
            window.setAttributes(windowParams);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /**
     * 设置对话框的宽和高
     *
     * @param width  宽度
     * @param height 高度
     */
    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        Dialog dialog = getDialog();
        if (null == dialog) {
            // 如果 dialog 为 null ，说明此时该对话框还未显示。将设置宽高的操作延迟到 onStart() 进行。
            return;
        }
        Window window = dialog.getWindow();
        if (null != window) {
            window.setLayout(mWidth, mHeight);
        }
    }

    /**
     * 返回对话框内容的布局
     *
     * @return 对话框内容布局
     */
    @LayoutRes
    protected abstract int getContentView();

    /**
     * 初始化对话框
     *
     * @param view 该对话框的视图对象
     */
    protected abstract void initView(View view);
}
