package com.jpfeng.baseframework.net;

import com.google.gson.JsonParseException;
import com.jpfeng.baseframework.R;
import com.jpfeng.baseframework.util.StringUtils;
import com.jpfeng.framework.data.net.util.ErrorParser;
import com.jpfeng.framework.data.net.util.NetError;
import com.jpfeng.framework.util.Logger;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
public class ErrorParserImpl implements ErrorParser {

    public static final int NET_ERR_UNKNOWN = 0;
    public static final int NET_ERR_TIMEOUT = 1;
    public static final int NET_ERR_NETWORK = 2;
    public static final int NET_ERR_HTTP = 3;
    public static final int NET_ERR_PARSE = 4;
    public static final int NET_ERR_SERVER = 4;

    @Override
    public void parse(Throwable t, NetError netError) {
        Logger.netDebug(t.getClass().getName() + ": " + t.getMessage());
        if (t instanceof UnknownHostException ||
                t instanceof SocketTimeoutException) {
            // 连接错误
            netError.setCode(NET_ERR_TIMEOUT);
            netError.setMessage(StringUtils.getString(R.string.net_error_timeout));
        } else if (t instanceof ConnectException) {
            // 网络错误
            netError.setCode(NET_ERR_NETWORK);
            netError.setMessage(StringUtils.getString(R.string.net_error_network));
        } else if (t instanceof HttpException) {
            // HTTP 错误
            netError.setCode(NET_ERR_HTTP);
            netError.setMessage(StringUtils.getString(R.string.net_error_http,
                    ((HttpException) t).code(), ((HttpException) t).message()));
        } else if (t instanceof JsonParseException) {
            // 解析错误
            netError.setCode(NET_ERR_PARSE);
            netError.setMessage(StringUtils.getString(R.string.net_error_json_parse));
        } else if (t instanceof ServerException) {
            // 内部错误
            netError.setCode(NET_ERR_SERVER);
            netError.setMessage(StringUtils.getString(R.string.net_error_api));
        } else {
            // 未知错误
            netError.setCode(NET_ERR_UNKNOWN);
            netError.setMessage(StringUtils.getString(R.string.net_error_unknown));
        }
    }
}
