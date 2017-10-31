package com.example.sashok.testapplication.network.model;

import android.support.annotation.Nullable;


public class ObjectResponse<T> extends Response implements DataProvider<T> {

    @Nullable
    T data;

    @Nullable
    @Override
    public T getData() {
        return data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }

}