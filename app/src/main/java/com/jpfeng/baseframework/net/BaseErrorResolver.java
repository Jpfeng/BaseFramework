package com.jpfeng.baseframework.net;

import com.google.gson.JsonParseException;
import com.jpfeng.baseframework.R;
import com.jpfeng.framework.data.net.util.ApiException;
import com.jpfeng.framework.data.net.util.ErrorResolver;
import com.jpfeng.framework.data.net.util.NetConfig;
import com.jpfeng.framework.util.Logger;
import com.jpfeng.baseframework.util.StringUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
public class BaseErrorResolver implements ErrorResolver {
    @Override
    public String resolveError(Throwable t) {
        Logger.debug(NetConfig.LOG_TAG, t.getClass().getName() + ": " + t.getMessage());
        if (t instanceof UnknownHostException ||
                t instanceof SocketTimeoutException) {
            // 连接错误
            return StringUtils.getString(R.string.net_error_timeout);
        } else if (t instanceof ConnectException) {
            // 网络错误
            return StringUtils.getString(R.string.net_error_network);
        } else if (t instanceof HttpException) {
            // HTTP 错误
            return StringUtils.getString(R.string.net_error_http,
                    ((HttpException) t).code(), ((HttpException) t).message());
        } else if (t instanceof JsonParseException) {
            // 解析错误
            return StringUtils.getString(R.string.net_error_json_parse);
        } else if (t instanceof ApiException) {
            // 内部错误
            return StringUtils.getString(R.string.net_error_api,
                    ((ApiException) t).getCode(), t.getMessage());
        } else {
            // 未知错误
            return StringUtils.getString(R.string.net_error_unknown);
        }
    }
}
