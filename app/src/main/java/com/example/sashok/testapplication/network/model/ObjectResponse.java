package com.example.sashok.testapplication.network.model;

import android.support.annotation.Nullable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;


public class ObjectResponse<T> extends Response implements DataProvider<T> {

    @Nullable
    T data;

    public void setData(@Nullable T data) {
        this.data = data;
    }

    @Nullable
    @Override
    public T getData() {
        return data;
    }

}