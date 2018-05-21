package com.mix.framework.util;

import android.view.Gravity;
import android.widget.Toast;

import com.mix.framework.base.BaseApplication;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/8
 */
public class ToastUtils {
    private ToastUtils() {
    }

    private static Toast mToast;
    private static Toast mToastCenter;

    public static void show(String content) {
        if (null == mToast) {
            mToast = Toast.makeText(BaseApplication.getContext(), content, Toast.LENGTH_SHORT);
        } else {
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setText(content);
        }
        mToast.show();
    }

    public static void showLong(String content) {
        if (null == mToast) {
            mToast = Toast.makeText(BaseApplication.getContext(), content, Toast.LENGTH_LONG);
        } else {
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setText(content);
        }
        mToast.show();
    }

    public static void showCenter(String content) {
        if (null == mToastCenter) {
            mToastCenter = mToastCenter.makeText(BaseApplication.getContext(), content, Toast.LENGTH_SHORT);
            mToastCenter.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToastCenter.setText(content);
        }
        mToastCenter.show();
    }

    public static void cancel() {
        if (null != mToast) {
            mToast.cancel();
        }
        if (null != mToastCenter) {
            mToastCenter.cancel();
        }
    }
}
