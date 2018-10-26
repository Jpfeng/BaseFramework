package com.mix.framework.util;

import androidx.annotation.StringRes;

import com.mix.framework.base.BaseApplication;

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
