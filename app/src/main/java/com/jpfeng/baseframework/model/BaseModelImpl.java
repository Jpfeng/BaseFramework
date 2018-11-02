package com.jpfeng.baseframework.model;

import com.jpfeng.baseframework.net.ErrorParserImpl;
import com.jpfeng.baseframework.net.NetResponse;
import com.jpfeng.baseframework.net.ServerException;
import com.jpfeng.framework.data.model.BaseModel;
import com.jpfeng.framework.data.model.IModelCallback;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
public abstract class BaseModelImpl<S> extends BaseModel<S> {

    protected <T> ResourceSubscriber<T> request(Flowable<NetResponse<T>> request,
                                                IModelCallback<T> callback) {
        return request(request, data -> data, callback);
    }

    protected <T, R> ResourceSubscriber<R> request(Flowable<NetResponse<T>> request,
                                                   Function<T, R> converter,
                                                   IModelCallback<R> callback) {
        return request(request, convertResult(), converter, callback, new ErrorParserImpl());
    }

    private <T> FlowableTransformer<NetResponse<T>, T> convertResult() {
        return upstream ->
                upstream.onErrorResumeNext((Function<Throwable, Flowable<NetResponse<T>>>) Flowable::error)
                        .flatMap((Function<NetResponse<T>, Flowable<T>>)
                                tNetResponse -> {
                                    if (tNetResponse.isError()) {
                                        return Flowable.error(new ServerException());
                                    } else {
                                        return emitData(tNetResponse.getResults());
                                    }
                                });
    }

    private static <T> Flowable<T> emitData(final T t) {
        try {
            return Flowable.just(t);
        } catch (Exception e) {
            return Flowable.error(e);
        }
    }
}
