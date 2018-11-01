package com.jpfeng.framework.data.net.util;

import java.io.Serializable;

import androidx.annotation.Nullable;

/**
 * Author: Jpfeng
 * E-mail: fengjupeng@whale.ws
 * Date: 2018/11/1
 */
public class NetError implements Serializable {

    public static final int DEFAULT_ERROR_CODE = -1;

    private final Throwable mRaw;
    private int mCode;
    private String mMessage;

    public NetError(Throwable raw) {
        this.mRaw = raw;
    }

    public void parseRaw(@Nullable ErrorParser parser) {
        if (null == parser) {
            mCode = DEFAULT_ERROR_CODE;
            mMessage = mRaw.getMessage();
        } else {
            parser.parse(mRaw, this);
        }
    }

    public void setCode(int code) {
        mCode = code;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public Throwable getRaw() {
        return mRaw;
    }
}
