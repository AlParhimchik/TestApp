package com.example.sashok.testapplication.network.model;

import android.support.annotation.Nullable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * Created by sashok on 26.10.17.
 */
@JsonObject
public class Response implements Serializable {
    @JsonField
    public int status;

    @Nullable
    @JsonField
    public String error;

    public int getErrorCode() {
        return error == null ? ErrorType.OK.getCode() : status;
    }

    @Nullable
    public String getErrorMessage() {
        return error == null ? null : error;
    }


}
