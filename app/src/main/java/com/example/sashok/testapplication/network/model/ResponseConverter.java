package com.example.sashok.testapplication.network.model;

import com.example.sashok.testapplication.ApiService;
import com.example.sashok.testapplication.App;
import com.example.sashok.testapplication.network.model.image.response.AddImageResponse;
import com.example.sashok.testapplication.network.model.image.response.DeleteImageResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.*;

/**
 * Created by sashok on 29.10.17.
 */

public class ResponseConverter {
    public static ObjectResponse convertErrorResponse(ResponseBody response, Class<? extends ObjectResponse> clazz) {
        ObjectResponse error=null;
        Converter<ResponseBody, ObjectResponse> errorConverter =
                ApiService.getRetrofit().responseBodyConverter(clazz, new Annotation[0]);
        try {
            error = errorConverter.convert(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return error;
    }
}
