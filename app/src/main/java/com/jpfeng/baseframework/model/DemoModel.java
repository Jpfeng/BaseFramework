package com.jpfeng.baseframework.model;

import com.jpfeng.baseframework.bean.DemoBean;
import com.jpfeng.baseframework.bean.ResultBean;
import com.jpfeng.baseframework.net.DemoApi;
import com.jpfeng.framework.data.model.IModelCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Author: Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2018/5/21
 */
public class DemoModel extends BaseModelImpl<DemoApi> {

    public ResourceSubscriber<List<DemoBean>> getToday(IModelCallback<List<DemoBean>> callback) {
        return request(mService.getToday(), stringListMap -> {
            ArrayList<ResultBean> all = new ArrayList<>();
            for (List<ResultBean> list : stringListMap.values()) {
                all.addAll(list);
            }
            // 洗一下牌，假装数据变了
            Collections.shuffle(all);
            ArrayList<DemoBean> results = new ArrayList<>();
            for (ResultBean item : all) {
                DemoBean bean = new DemoBean();
                bean.setId(item.get_id());
                bean.setDesc(item.getDesc());
                List<String> images = item.getImages();
                if (null != images && images.size() > 0) {
                    bean.setImage(images.get(0));
                }
                bean.setWho(item.getWho());
                bean.setPublishedAt(item.getPublishedAt().split("T")[0]);
                bean.setType(item.getType());
                bean.setUrl(item.getUrl());
                results.add(bean);
            }
            return results;
        }, callback);
    }
}
