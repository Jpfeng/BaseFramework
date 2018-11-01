package com.jpfeng.baseframework.util;

import com.jpfeng.framework.base.BaseApplication;

import androidx.annotation.StringRes;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/8
 */
public class StringUtils {
    private StringUtils() {
    }

    public static String getString(@StringRes int stringResId) {
        return BaseApplication.getContext().getString(stringResId);
    }

    public static String getString(@StringRes int resId, Object... formatArgs) {
        return BaseApplication.getContext().getString(resId, formatArgs);
    }
}
