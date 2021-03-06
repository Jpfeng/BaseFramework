package com.jpfeng.framework.data.model;

import com.jpfeng.framework.util.ApplicationUtils;

import java.util.Hashtable;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Model 统一管理类
 * <p>
 * Author: Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2018/5/21
 */
public class ModelManager {

    /**
     * Model 的注册表
     */
    private static final Map<String, BaseModel> mModelMap = new Hashtable<>();

    private ModelManager() {
    }

    /**
     * 获取 Model 的实例
     *
     * @param clazz Model 的类型
     * @param <T>   model 类型的泛型
     * @return Model 的实例
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> T get(@NonNull final Class<T> clazz) {
        ApplicationUtils.checkNonNull(clazz);
        final String className = clazz.getName();
        if (!mModelMap.containsKey(className)) {
            synchronized (ModelManager.class) {
                if (!mModelMap.containsKey(className)) {
                    try {
                        return clazz.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                        return null;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return (T) mModelMap.get(className);
    }

    /**
     * 重新构建所有已注册的 Model。一般用于网络设置变更
     */
    public static void reconstructModels() {
        for (BaseModel model : mModelMap.values()) {
            model.construct();
        }
    }

    /**
     * 注册 Model。ModelName 必须为唯一。
     *
     * @param modelName Model 的类名
     * @param model     Model 的实例
     */
    static void register(@NonNull String modelName, @NonNull BaseModel model) {
        mModelMap.put(modelName, model);
    }

    /**
     * 注册表中是否存在该 Model
     *
     * @param modelName Model 的类名
     * @return true - 存在  false - 不存在
     */
    static boolean contains(String modelName) {
        return mModelMap.containsKey(modelName);
    }
}
