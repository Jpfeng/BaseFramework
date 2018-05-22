package com.mix.baseframework.model;

import com.mix.baseframework.net.BaseErrorResolver;
import com.mix.baseframework.net.NetResponse;
import com.mix.framework.data.model.BaseModel;
import com.mix.framework.data.model.IModelCallback;
import com.mix.framework.data.net.util.ApiException;

import io.reactivex.BackpressureStrategy;
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
        return request(request, convertResult(), converter, callback, new BaseErrorResolver());
    }

    private <T> FlowableTransformer<NetResponse<T>, T> convertResult() {
        return upstream ->
                upstream.onErrorResumeNext((Function<Throwable, Flowable<NetResponse<T>>>) Flowable::error)
                        .flatMap((Function<NetResponse<T>, Flowable<T>>)
                                tNetResponse -> {
                                    if (200 == tNetResponse.getCode()) {
                                        return emitData(tNetResponse.getData());
                                    } else {
                                        return Flowable.error(new ApiException(tNetResponse.getCode(), tNetResponse.getMsg()));
                                    }
                                });
    }

    private static <T> Flowable<T> emitData(final T t) {
        return Flowable.create(emitter -> {
            try {
                emitter.onNext(t);
            } catch (Exception e) {
                emitter.onError(e);
            }
        }, BackpressureStrategy.BUFFER);
    }
}
