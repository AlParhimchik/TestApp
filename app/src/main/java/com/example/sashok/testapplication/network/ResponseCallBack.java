package com.example.sashok.testapplication.network;

/**
 * Created by sashok on 31.10.17.
 */

public interface ResponseCallBack<T> {
    public void onResponse(T t);

    public void onError(Throwable t);
}
