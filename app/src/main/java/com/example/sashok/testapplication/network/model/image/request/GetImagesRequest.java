package com.example.sashok.testapplication.network.model.image.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.example.sashok.testapplication.network.model.Request;

import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;

/**
 * Created by sashok on 28.10.17.
 */

@JsonObject
public class GetImagesRequest extends Request {
    @JsonField
    private int page;

    public GetImagesRequest() {
    }

    public GetImagesRequest(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
