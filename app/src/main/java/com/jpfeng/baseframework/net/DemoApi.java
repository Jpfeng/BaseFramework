package com.jpfeng.baseframework.net;

import com.jpfeng.baseframework.bean.ResultBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
public interface DemoApi {
    String TODAY = "today";

    @GET(TODAY)
    Flowable<NetResponse<Map<String, List<ResultBean>>>> getToday();
}
