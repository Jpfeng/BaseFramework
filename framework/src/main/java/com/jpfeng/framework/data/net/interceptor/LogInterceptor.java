package com.jpfeng.framework.data.net.interceptor;

import android.text.TextUtils;

import com.jpfeng.framework.BuildConfig;
import com.jpfeng.framework.util.Logger;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * 显示网络请求日志的拦截器
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/4
 */
public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (BuildConfig.DEBUG) {
            Request request = chain.request();
            long start = System.currentTimeMillis();
            Response response = chain.proceed(request);
            long end = System.currentTimeMillis();
            Logger.netDebug(getLogContent(request, response, end - start));
            return response;
        } else {
            return chain.proceed(chain.request());
        }
    }

    private String getLogContent(Request request, Response response, long time) throws IOException {
        StringBuilder result = new StringBuilder();
        // request 基本信息
        result.append("net debug log")
                .append("\n========= Request =========")
                .append("\nmethod: ").append(request.method())
                .append("\nurl: ").append(request.url().toString());

        // request headers
        Headers headers = request.headers();
        result.append("\nheaders {\n");
        for (int i = 0, size = headers.size(); i < size; i++) {
            result.append("\t").append(headers.name(i)).append(": ").append(headers.value(i)).append("\n");
        }
        result.append("}");

        // request post body
        if (TextUtils.equals("POST", request.method())) {
            result.append("\nbody ");
            try {
                final Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                result.append(buffer.readUtf8());
            } catch (final IOException e) {
                result.append("{print did not work}");
            }
        }

        // response 基本信息
        ResponseBody peekBody = response.peekBody(1024);
        result.append("\n========= Response =========")
                .append("\ncode: ").append(response.code())
                .append("\nmessage: ").append(response.message())
                .append("\njson: ").append(peekBody.string());

        // 时间
        if (result.charAt(result.length() - 1) != '\n') {
            result.append("\n");
        }
        result.append("========= time: ").append(time).append("ms =========");
        return result.toString();
    }
}
