package com.jpfeng.baseframework.net;

import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
public interface DemoApi {

    @GET(SEARCH_TIPS)
    Flowable<NetResponse<String>> getSearchTips();

    String SEARCH_TIPS = "home/searchRecommend";//获取首页的搜索提示
}
