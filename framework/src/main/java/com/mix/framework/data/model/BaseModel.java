package com.mix.framework.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mix.framework.data.net.NetClient;
import com.mix.framework.data.net.util.ErrorResolver;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/8
 */
public abstract class BaseModel<S> {
    /**
     * Retrofit 的 Service 对象
     */
    protected S mService;

    /**
     * 将构造方法声明为 Protected 。不可手动调用构造方法进行实例化，保证统一使用 ModelManager 管理。
     */
    protected BaseModel() {
        final String className = this.getClass().getName();
        if (ModelManager.contains(className)) {
            // 如果已注册该实例，则抛出异常。
            throw new RuntimeException("Can not create instance for class " + className
                    + ", instance already exists!");
        } else {
            // 如果实例不存在，则进入同步代码。
            synchronized (BaseModel.class) {
                if (ModelManager.contains(className)) {
                    // 双重判断。如果已存在该实例，则抛出异常。
                    throw new RuntimeException("Can not create instance for class " + className
                            + ", instance already exists!");
                } else {
                    // 注册实例
                    ModelManager.register(className, this);
                }
            }
        }
        // 进行初始化
        construct();
    }

    /**
     * 进行 http 网络请求的方法。
     *
     * @param request       由 retrofit 返回的 Flowable 对象
     * @param dataGetter    从 Response 中提取数据的方法
     * @param dataConverter 将数据进行转换的方法。运行在 IO 线程
     * @param callback      结果的回调。运行在主线程
     * @param resolver      错误异常的解析类。如果为空，则返回 Throwable#getMessage()
     * @param <P>           通用服务器返回类型的泛型
     * @param <D>           从服务器返回中提取数据的泛型
     * @param <R>           最终结果的泛型
     * @return 网络请求的订阅
     */
    protected <P, D, R> ResourceSubscriber<R> request(@NonNull Flowable<P> request,
                                                      @NonNull FlowableTransformer<P, D> dataGetter,
                                                      @NonNull Function<D, R> dataConverter,
                                                      @NonNull IModelCallback<R> callback,
                                                      @Nullable ErrorResolver resolver) {
        return request.subscribeOn(Schedulers.io())
                .compose(dataGetter)
                .map(dataConverter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceSubscriber<R>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        callback.onStart();
                    }

                    @Override
                    public void onNext(R r) {
                        callback.onSuccess(r);
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (null != resolver) {
                            callback.onError(resolver.resolveError(t));
                        } else {
                            callback.onError(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 初始化 Model ，创建 Service
     */
    protected void construct() {
        mService = NetClient.getInstance().getRetrofitClient().create(getService());
    }

    /**
     * 返回 Service 的类型。每个 Model 对应一个 Service 。
     *
     * @return Service 类
     */
    @NonNull
    protected abstract Class<S> getService();
}
