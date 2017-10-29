package com.example.sashok.testapplication.network;

import com.example.sashok.testapplication.network.model.image.request.AddImageRequest;
import com.example.sashok.testapplication.network.model.image.response.AddImageResponse;
import com.example.sashok.testapplication.network.model.image.response.DeleteImageResponse;
import com.example.sashok.testapplication.network.model.image.response.GetImageResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.example.sashok.testapplication.network.Constance.IMAGE;

/**
 * Created by sashok on 26.10.17.
 */

public interface ImageAdapter {
    @GET(IMAGE)
    @Headers("Content-type: application/json")
    Call<GetImageResponse> getImages(@Query("page") int page, @Header("Access-Token") String token);

    @POST(IMAGE)
    @Headers("Content-type: application/json")
    Call<AddImageResponse> addImage(@Body AddImageRequest request, @Header("Access-Token") String token);

    @DELETE(IMAGE + "/{id}")
    @Headers("Content-type: application/json")
    Call<DeleteImageResponse> deleteImage(@Path("id") int imageI, @Header("Access-Token") String token);
}
