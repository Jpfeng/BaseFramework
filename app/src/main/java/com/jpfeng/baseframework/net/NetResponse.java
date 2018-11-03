package com.jpfeng.baseframework.net;

import java.util.List;

/**
 * Author: Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2018/5/21
 */
public class NetResponse<T> {

    private List<String> category;
    private int count;
    private boolean error;
    private T results;

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
